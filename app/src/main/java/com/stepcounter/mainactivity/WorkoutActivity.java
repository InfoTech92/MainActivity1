package com.stepcounter.mainactivity;

import org.json.JSONObject;

import com.example.mainactivity.R;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This activity is the main workout activity: it shows the actual workout level, workout time
 * and suggests when the user should accelerate or decelerate.
 * From this activity it is possible to pause and resume the workout.
 *
 */
public class WorkoutActivity extends ActionBarActivity implements Animation.AnimationListener, View.OnClickListener {
	
	
	/**
	 * This field defines a "zoom in" animation.
	 */
    private Animation zoom_in;
    /**
     * This field defines a "zoom out" animation.
     */
    private Animation zoom_out;
    /**
     * This field defines the workout start button.
     */
    private ImageView btnSart;
    /**
     * This field defines the workout stop button.
     */
    private ImageView btnStop;
    /**
     * This field defines the left shoe image.
     */
    private ImageView shoeImage1;
    /**
     * This field defines the right shoe image.
     */
    private ImageView shoeImage2;
    
    
    /**
     * Timer thread.
     */
    private UpdateTimerThread updateTimerThread;
    
    /**
     * Step counting thread.
     */
	private StepCountingThread stepCountingThread;
    
    /**
     * This boolean value indicates if the user is actually running.
     */
    private boolean running;
    
    
    /**
     * Actual Workout object.
     */
    public Workout workout;
    
    
    
    public TextView textStepCount;
    
    
    public TextView textViewCurrentSpeedKmH;
    public TextView textViewKm;
    public TextView textViewMeanSpeedKmH;
    public TextView textViewCurrentSpeedStepsMin;
    public TextView textViewMeanSpeedLastX;
    public TextView textViewStepsNumberLastX;
    public TextView userNameWorkoutActivity;
    public TextView stepLengthWorkoutActivity;
    
    public Handler handler;
    
    
    public boolean userFound;
    
    public Profile user;
    
    public VerticalBar seekBar1;
    
    
    public ActionBarActivity self;
    
    
    public long lastTime;
    
    
    public boolean vocalsActive;
    
    
    public int stepsC;
    
    /**
     * OnCreate method implementation.
     */
    @SuppressLint("HandlerLeak")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //Images initialization...
        this.shoeImage1 = (ImageView) findViewById(R.id.shoeImage1);
        this.shoeImage2 = (ImageView) findViewById(R.id.shoeImage2);

        
        this.textStepCount = (TextView) findViewById(R.id.textStepCount);

        //Animations are loaded and their listeners are set up
        this.zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        this.zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        this.zoom_in.setAnimationListener(this);
        this.zoom_out.setAnimationListener(this);


        //Buttons initialization...
        this.btnSart = (ImageView) findViewById(R.id.btnStart);
        this.btnStop = (ImageView) findViewById(R.id.btnStop);
        
        this.btnSart.setOnClickListener(this);
        this.btnStop.setOnClickListener(this);
        
        //Running flag initialization
        this.running = false;
        
        
        this.updateTimerThread = new UpdateTimerThread();
        
        
        this.textViewCurrentSpeedKmH 	  = (TextView) findViewById(R.id.textViewCurrentSpeedKmH);
        this.textViewKm 				  = (TextView) findViewById(R.id.textViewKm);
        this.textViewMeanSpeedKmH 		  = (TextView) findViewById(R.id.textViewMeanSpeedKmH);
        this.textViewCurrentSpeedStepsMin = (TextView) findViewById(R.id.textViewCurrentSpeedStepsMin);
        this.textViewMeanSpeedLastX 	  = (TextView) findViewById(R.id.textViewMeanSpeedLastX);
        this.textViewStepsNumberLastX 	  = (TextView) findViewById(R.id.textViewStepsNumberLastX);
        
        this.userNameWorkoutActivity 	= (TextView) findViewById(R.id.userNameWorkoutActivity);
        this.stepLengthWorkoutActivity 	= (TextView) findViewById(R.id.stepLengthWorkoutActivity);
        
        
        
        
        
        
        this.seekBar1 = (VerticalBar) findViewById(R.id.seekBar1);
        
        
        
        
        
        Intent i = this.getIntent();
        
        this.workout = new Workout(0,i.getStringExtra("workoutName"),i.getStringExtra("workoutJson"));
        int stepLen = i.getIntExtra("stepLen", Profile.DEFAULT_STEP_LEN);
        
        self = this;
        
        JSONObject json = workout.getWorkoutJSON();
        
        stepsC = json.getInt("expected_mean_speed");
        
        
        this.lastTime = SystemClock.uptimeMillis();
        this.vocalsActive = true;
        
        this.handler = new Handler(){
        	
        	@Override
        	public void handleMessage(Message msg){
        		String steps 			  = "" + stepCountingThread.mListener.stepsNumberOut;
        		String currentSpeedKmH 	  = "" + stepCountingThread.mListener.currentSpeedKmHOut;
        		String totalDistance 	  = "" + stepCountingThread.mListener.totalDistanceOut;
        		String meanSpeedKmH 	  = "" + stepCountingThread.mListener.meanSpeedKmHOut;
        		String stepsPerMinute 	  = "" + stepCountingThread.mListener.stepsPerMinuteOut;
        		String meanSpeedLastX 	  = "" + stepCountingThread.mListener.meanSpeedLastXOut;
        		String stepsInLastXMeters = "" + stepCountingThread.mListener.stepsInLastXMetersOut;
        		
        		
        		
        		textStepCount.setText(steps);
        		textViewCurrentSpeedKmH.setText(currentSpeedKmH);
        	    textViewKm.setText(totalDistance);
        	    textViewMeanSpeedKmH.setText(meanSpeedKmH);
        	    textViewCurrentSpeedStepsMin.setText(stepsPerMinute);
        	    textViewMeanSpeedLastX.setText(meanSpeedLastX);
        	    textViewStepsNumberLastX.setText(stepsInLastXMeters);
        	    
        	    
        	    int delta;
        	    int tollerance = 10;
                int maxTollerance = 50;
                
                int secondsMessageExceededTollerance = 30;
                int secondsMessageExceededMaxTollerance = 15;
                
                
        	    delta = Math.abs(stepsC - (int) stepCountingThread.mListener.stepsPerMinuteOut);
        	    
        	    if(delta >= tollerance){
        	    	seekBar1.setProgressDrawable((Drawable) self.getResources().getDrawable(R.drawable.background_fill));
        	    	if(delta >= maxTollerance){
        	    		delta = maxTollerance;
        	    		
        	    		long currentTime = SystemClock.uptimeMillis();
        	    		long timeDelta = Math.abs(currentTime - lastTime);
        	    		
        	    		if(timeDelta*1000 >= secondsMessageExceededMaxTollerance){
        	    			//vocal 
        	    			lastTime = currentTime;
        	    		}
        	    		
        	    		
        	    	}else{
        	    		if(delta >= tollerance){
        	    			long currentTime = SystemClock.uptimeMillis();
            	    		long timeDelta = Math.abs(currentTime - lastTime);
            	    		
            	    		if(timeDelta*1000 >= secondsMessageExceededTollerance){
            	    			//vocal 
            	    			lastTime = currentTime;
            	    		}
            	    		
        	    		}
        	    		
        	    		
        	    	}
        	    
        	    }else{
        	    	seekBar1.setProgressDrawable((Drawable) self.getResources().getDrawable(R.drawable.background_fill));
        	    }
        	    
        	    int indexMaxVal = seekBar1.getMax();
        	    
        	    int progress = (int) stepCountingThread.mListener.stepsPerMinuteOut * indexMaxVal / (2 * stepsC );
        	    
        	    progress = progress >= indexMaxVal ? indexMaxVal : progress;
        	    
        	    seekBar1.setProgress(progress);		
        	    		
        	    stepCountingThread.mListener.readFlag = true;
        	}
        };
        
        
        
        
        
        this.updateTimerThread.timerValue = (TextView) findViewById(R.id.textViewClock);
        
        
        
        
        this.stepCountingThread = new StepCountingThread(this, handler, stepLen);
        
        Thread thread = new Thread(stepCountingThread);
        thread.start();
        
    }



    /**
     * AnimationListener method implementation.
     */
    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     * AnimationListener method implementation.
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        
    }
    
    
    /**
     * AnimationListener method implementation.
     */
    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * OnClickListener method implementation.
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
        	
        	//When the start button is pressed the animation shows up.
            case R.id.btnStart:
            	
            	if(this.running){
            		
            		this.shoeImage1.clearAnimation();
                	this.shoeImage2.clearAnimation();
                	
                	Drawable playButtonIcon = getResources().getDrawable(R.drawable.play_button_image);
                	this.btnSart.setImageDrawable(playButtonIcon);
                	
                	this.stepCountingThread.mListener.stop();
                	this.updateTimerThread.timerPause();
                	
                	
            	}else{
            		this.shoeImage1.startAnimation(this.zoom_in);
            		this.shoeImage2.startAnimation(this.zoom_out);
            		
            		Drawable pauseButtonIcon = getResources().getDrawable(R.drawable.pause_button_image);
                	this.btnSart.setImageDrawable(pauseButtonIcon);
                	
                	this.stepCountingThread.mListener.resume();
                	this.updateTimerThread.timerResume();

            	}
            	
            	
            	this.running = ! this.running;
            	
            	
            break;

            
            
            
            //When the stop button is pressed the animation is interrupted
            //and the WorkoutEndActivity is launched.
            case R.id.btnStop:

            	this.shoeImage1.clearAnimation();
            	this.shoeImage2.clearAnimation();
            	
            	
            	this.saveData();
            	
            	Intent i = new Intent(this, WorkoutEndActivity.class);
            	
    	    	i.putExtra("workoutName", this.workout.getWorkoutName());
    	    	i.putExtra("workoutJson", this.workout.getWorkoutJSONString());
            	
				startActivity(i);
				
				this.finish();
            break;
                

        }
    };
    
    
    
    
    
    public void saveData(){
    	
		
		String timeSamplings = "[";
		int i;
		for(i = 0 ; i < this.stepCountingThread.mListener.timeSamplings.size() -1; i++){
			timeSamplings = timeSamplings + this.stepCountingThread.mListener.timeSamplings.get(i) + ", ";
		}
		timeSamplings = timeSamplings + this.stepCountingThread.mListener.timeSamplings.get(i) + "]";
		
		
		String speedKmHSamplings = "[";
		for(i = 0 ; i < this.stepCountingThread.mListener.speeds.size() -1 ; i++ ){
			speedKmHSamplings += this.stepCountingThread.mListener.speeds.get(i);
		}
		speedKmHSamplings += this.stepCountingThread.mListener.speeds.get(i) + "]";
		
		
		String speedStepMinSamplings = "[";
		for(i = 0 ; i < this.stepCountingThread.mListener.speedStepsPerMinuteSamplings.size() -1 ; i++ ){
			speedStepMinSamplings += this.stepCountingThread.mListener.speedStepsPerMinuteSamplings.get(i);
		}
		speedStepMinSamplings += this.stepCountingThread.mListener.speedStepsPerMinuteSamplings.get(i) + "]";
		
		String footElevationCmSamplings = "[";
		for(i = 0 ; i < this.stepCountingThread.mListener.legElevationSamplings.size() -1 ; i++ ){
			footElevationCmSamplings += this.stepCountingThread.mListener.legElevationSamplings.get(i);
		}
		footElevationCmSamplings += this.stepCountingThread.mListener.legElevationSamplings.get(i) + "]";
		
		String distanceVariationSamplings = "[";
		for(i = 0 ; i < this.stepCountingThread.mListener.motionsAccelerometer.size() -1 ; i++ ){
			distanceVariationSamplings += this.stepCountingThread.mListener.motionsAccelerometer.get(i);
		}
		distanceVariationSamplings += this.stepCountingThread.mListener.motionsAccelerometer.get(i) + "]";
		
		
		String json = 	"{" +	
							"\"total_distance\":\"" +this.stepCountingThread.mListener.distanceKmOut + "\"," + 
							
							"\"steps\":\"" + this.stepCountingThread.mListener.steps + "\"," + 
							
							"\"kcals\":\"" + this.stepCountingThread.mListener.kcal + "\"," + 
							
							"\"expected_mean_speed\":\"" + stepsC + "\"," +
							
							"\"mean_speeed_km_h\":\"" + this.stepCountingThread.mListener.meanSpeedKmHOut + "\"," +			
							"\"mean_speed_step_min\":\"" + (this.stepCountingThread.mListener.steps /  (this.stepCountingThread.mListener.trainingTime*1000000))*60 + "\"," + 			
							"\"mean_foot_elevation_cm\":\"" + (this.stepCountingThread.mListener.totalLegElevation) / (this.stepCountingThread.mListener.i) + "\"," +	
							
							"\"mean_speeed_km_h_last_x\":\"" + this.stepCountingThread.mListener.meanSpeedLastXOut * 3.6 + "\"," +		
							"\"mean_speed_step_min_last_x\":\"" + (this.stepCountingThread.mListener.stepsInLastXMetersOut / (this.stepCountingThread.mListener.samplingsSum * StepCountingThread.DELAY)) + "\"," +
							
							"\"time_sampling_frequence\":\""+ StepCountingThread.DELAY *1000 +"\"," +
						
							"\"time_samplings\":[\"" + timeSamplings + "\"]," +
							"\"speed_km_h_samplings\":[\""+ speedKmHSamplings +"\"]," + 
							"\"speed_step_min_samplings\":[\"" + speedStepMinSamplings + "\"]," +
							"\"foot_elevation_cm_samplings\":[\"" + footElevationCmSamplings + "\"],"+
							"\"distance_variation_samplings\":[\"" + distanceVariationSamplings + "\"]" + 
						"}" ;
						
		//The new JSONObject is created from the given string-
		this.workout.setWorkoutJSON(json);
		
		int lastSample = this.stepCountingThread.mListener.timeSamplings.size() -1;
		
		this.user.setStepsTot(this.user.getStepsTot() + this.stepCountingThread.mListener.steps );
		this.user.setTotalDistanceKm(this.user.getTotalDistanceKm() + this.stepCountingThread.mListener.distanceKmOut);
		this.user.setTotalKcal(this.user.getTotalKcal() + this.stepCountingThread.mListener.kcal);
		this.user.setTotalTimeSec(this.user.getTotalTimeSec() + this.stepCountingThread.mListener.timeSamplings.get(lastSample));
		
		
		
    	
    }
    
    
    
    
    
    
    
    
    
    /**
	 * Creates the options menu.
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    /**
	 * Set the click listeners for the menu items.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    

    
    public void fetchUserData(){
    	//A database adapter: this is useful to know the user list in the database.
        DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor profiles = dbAdapter.fetchAllProfiles();
        
        this.userFound = profiles.moveToFirst();
        
        if(this.userFound){
        	this.user = new Profile();
        	this.user.setId(profiles.getInt(profiles.getColumnIndex("_id")));
        	this.user.setName(profiles.getString(profiles.getColumnIndex("name")));
        	this.user.setSurname(profiles.getString(profiles.getColumnIndex("surname")));
        	this.user.setGender(profiles.getString(profiles.getColumnIndex("gender")));
        	this.user.setHeight(profiles.getInt(profiles.getColumnIndex("height")));
        	this.user.setWeight(profiles.getInt(profiles.getColumnIndex("weight")));
        	this.user.setStepLen(profiles.getInt(profiles.getColumnIndex("step_len_cm")));
        	this.user.setTotalDistanceKm(profiles.getInt(profiles.getColumnIndex("tot_dist_km")));
        	this.user.setStepsTot(profiles.getInt(profiles.getColumnIndex("tot_steps")));
        	this.user.setTotalTimeSec(profiles.getInt(profiles.getColumnIndex("tot_time_sec")));
        	this.user.setTotalKcal(profiles.getInt(profiles.getColumnIndex("tot_kcal")));
        	this.user.setX(profiles.getInt(profiles.getColumnIndex("x")));
        	this.user.setAvatarPath(profiles.getString(profiles.getColumnIndex("avatar_path")));
        }else{
        	this.user = null;
        }
        
        dbAdapter.close();
    }
    
    
	
}
