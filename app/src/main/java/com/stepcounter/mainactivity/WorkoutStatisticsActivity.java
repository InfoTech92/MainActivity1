package com.stepcounter.mainactivity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.example.mainactivity.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This activity shows a training statistics.
 *
 */
public class WorkoutStatisticsActivity extends ActionBarActivity implements OnClickListener{
	
	
	
	public static final int TIME_SAMPLINGS 				 = 1;
	public static final int SPEED_KM_H_SAMPLINGS 		 = 2;
	public static final int SPEED_STEPS_MIN_SAMPLINGS 	 = 3;
	public static final int FOOT_ELEVATION_CM_SAMPLINGS  = 4;
	public static final int DISTANCE_VARIATION_SAMPLINGS = 5;
	
	
	public TextView textViewKm;
	public TextView textViewTotalSteps;
	public TextView textViewTotalTime;
	public TextView textViewTotalKcals;
	public TextView textViewMeanSpeedKmH;
	public TextView textViewCurrentSpeedStepsMin;

	
	public Workout workout;
	
	
	public ImageView avatar;
	
	
	public Profile user;
	
	public boolean userFound;
	
	
	public JSONObject jsonWorkout;
	
	/**
	 * OnCreate method.
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistics);
        
        
        this.textViewKm = (TextView) findViewById(R.id.textViewKm);
        this.textViewTotalSteps = (TextView) findViewById(R.id.textViewTotalSteps);
        this.textViewTotalTime = (TextView) findViewById(R.id.textViewTotalTime);
        this.textViewTotalKcals = (TextView) findViewById(R.id.textViewTotalKcals);
        this.textViewMeanSpeedKmH = (TextView) findViewById(R.id.textViewMeanSpeedKmH);
        this.textViewCurrentSpeedStepsMin = (TextView) findViewById(R.id.textViewCurrentSpeedStepsMin);
        
        this.avatar = (ImageView) findViewById(R.id.circularImageWorkoutStatisticsActivity);
        
        fetchUserData();
        
        Intent i = this.getIntent();
        this.workout = new Workout(i.getIntExtra("workout_id", 0),i.getStringExtra("workout_name"),i.getStringExtra("workout_json"));
        
        this.jsonWorkout = this.workout.getWorkoutJSON();
        
        /*
        String json = "{" +	"\"total_distance\":\"0\"," + 
							
							"\"steps\":\"0\"" +
							
							"\"
									
							"\"expected_mean_speed\":\""+exp_mean_speed+"\"," +
							 
							
							"\"mean_speeed_km_h\":\"0\"," +			
							"\"mean_speed_step_min\":\"0\"," + 			
							"\"mean_foot_elevation_cm\":\"0\"," +	
							
							"\"mean_speeed_km_h_last_x\":\"0\"," +		
							"\"mean_speed_step_min_last_x\":\"0\"," +
							"\"mean_foot_elevation_cm\":\"0\"," +
							
							"\"time_sampling_frequence\":\"40\"," +
						
							"\"time_samplings\":[\"0\"]," +
							"\"speed_km_h_samplings\":[\"0\"]," + 
							"\"speed_step_min_samplings\":[\"0\"]," +
							"\"foot_elevation_cm_samplings\":[\"0\"],"+
							"\"distance_variation_samplings\":[\"0\"]" + 
						"}" ; */
        
        int totalKm = this.jsonWorkout.getInt("total_distance");
        int totalSteps = this.jsonWorkout.getInt("steps");
        int totalTimeSecs = 1000000 * this.jsonWorkout.getJSONArray("time_samplings").getInt(this.jsonWorkout.getJSONArray("time_samplings").length()-1);
        double totalKcals = this.jsonWorkout.getDouble("kcals");
        double meanSpeedKmH = this.jsonWorkout.getDouble("mean_speeed_km_h");
        double meanSpeedStepsMin = this.jsonWorkout.getDouble("mean_speed_step_min");
        
        this.textViewKm.setText(totalKm);
        this.textViewTotalSteps.setText(totalSteps);
        this.textViewTotalTime.setText(totalTimeSecs);
        this.textViewTotalKcals.setText(Double.toString(totalKcals));
        this.textViewMeanSpeedKmH.setText(Double.toString(meanSpeedKmH));
        this.textViewCurrentSpeedStepsMin.setText(Double.toString(meanSpeedStepsMin));
        
        this.updateUserData();
	}
	
	
	public void setGraph(int x, int y){
		
		
		JSONArray timeSamplings = this.jsonWorkout.getJSONArray("time_samplings");
		JSONArray speedKmHSamplings = this.jsonWorkout.getJSONArray("speed_km_h_samplings");
		JSONArray speedStepMinSamplings = this.jsonWorkout.getJSONArray("speed_step_min_samplings");
		JSONArray footElevationCmSamplings = this.jsonWorkout.getJSONArray("foot_elevation_cm_samplings");
		JSONArray distanceVariationSamplings = this.jsonWorkout.getJSONArray("distance_variation_samplings");
		
		JSONArray xArray = null;
		JSONArray yArray = null;
		
		switch(x){
			case TIME_SAMPLINGS:
				xArray = timeSamplings;
			break;
			
			case SPEED_KM_H_SAMPLINGS:
				xArray = speedKmHSamplings;
			break;
			
			case SPEED_STEPS_MIN_SAMPLINGS:
				xArray = speedStepMinSamplings;
			break;
			
			case FOOT_ELEVATION_CM_SAMPLINGS:
				xArray = footElevationCmSamplings;
			break;
			
			case DISTANCE_VARIATION_SAMPLINGS:
				xArray = distanceVariationSamplings;
			break;
		}
		
		switch(y){
			case TIME_SAMPLINGS:
				yArray = timeSamplings;
			break;
			
			case SPEED_KM_H_SAMPLINGS:
				yArray = speedKmHSamplings;
			break;
			
			case SPEED_STEPS_MIN_SAMPLINGS:
				yArray = speedStepMinSamplings;
			break;
			
			case FOOT_ELEVATION_CM_SAMPLINGS:
				yArray = footElevationCmSamplings;
			break;
			
			case DISTANCE_VARIATION_SAMPLINGS:
				yArray = distanceVariationSamplings;
			break;
		}
		
		DataPoint[] dataPoint = new DataPoint[timeSamplings.length()];
		
		for(int i = 0; i < timeSamplings.length() ; i++){
			dataPoint[i] = new DataPoint(xArray.getDouble(i), yArray.getDouble(i));
		}
		
		//Graph creation.
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
        graph.addSeries(series);
        
	}
	
	
	
	/**
	 * OnClickListener method implementation.
	 */
	@Override
	public void onClick(View v) {
		
		
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
    
	
	
    private void updateUserData(){
    	
    	String name 	     = this.user.getName();
		String surname		 = this.user.getSurname();
	    String gender 		 = this.user.getGender();
	    int height  		 = this.user.getHeight();
	    int weight  		 = this.user.getWeight();
	    int stepLen 		 = this.user.getStepLen();
	    int x		 		 = this.user.getX();
	    String avatarPath 	 = this.user.getAvatarPath() == null ? "" : this.user.getAvatarPath();
	    int totalDistanceKm  = this.user.getTotalDistanceKm();
		int stepsTot 		 = this.user.getStepsTot();
		int totalTimeSec 	 = this.user.getTotalTimeSec();
		double kcalTot 		 = this.user.getTotalKcal();
		int id 			 	 = this.user.getId();
    
    
		DatabaseAdapter dbAdapter = new DatabaseAdapter(this.getApplicationContext());
		dbAdapter.open();
		
		if(! this.userFound){
			dbAdapter.createNewProfile(name, surname, gender, height, weight, stepLen, 0, 0, 0, 0, x, avatarPath);
		}else{
			dbAdapter.updateProfile(id, name, surname, gender, height, weight, stepLen, totalDistanceKm, stepsTot, totalTimeSec, kcalTot, x, avatarPath);
		}
			
		dbAdapter.close();
		
		
    }
	
}
