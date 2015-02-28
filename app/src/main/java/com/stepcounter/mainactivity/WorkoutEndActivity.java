package com.stepcounter.mainactivity;

import java.io.File;
import java.io.FileOutputStream;

import com.example.mainactivity.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity is launched when the workout is terminated by the user.
 *
 */
public class WorkoutEndActivity extends ActionBarActivity implements OnClickListener{
	
	/**
	 * This object contains data which are relative to the workout 
	 * that has just ended.
	 */
	public Workout workout;
	
	/**
	 * OnCreate method implementation.
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_end);
        
        //Buttons initialization...
        Button btnShareEndWorkout = (Button) findViewById(R.id.btnShareEndWorkout);
        Button btnGraphicEndWorkout = (Button) findViewById(R.id.btnGraphicEndWorkout);
        Button btnExitNoSaveEndWorkout = (Button) findViewById(R.id.btnExitNoSaveEndWorkout);
        Button btnExitSaveEndWorkout = (Button) findViewById(R.id.btnExitSaveEndWorkout);
        
        btnShareEndWorkout.setOnClickListener(this);
        btnGraphicEndWorkout.setOnClickListener(this);
        btnExitNoSaveEndWorkout.setOnClickListener(this);
        btnExitSaveEndWorkout.setOnClickListener(this);
        
        Intent i = getIntent();
        
        this.workout = new Workout(0,i.getStringExtra("workoutName"),i.getStringExtra("workoutJson"));
        
        
	}

	/**
	 * OnclickListener method implementation.
	 */
	@Override
	public void onClick(View v) {
		
		Intent i = null;
		
		switch(v.getId()){
		
			/*
			 * If the share button is pressed, the workout can be sent via email.
			 */
			case R.id.btnShareEndWorkout:
				
				String jsonString = "{"
						+ "\"name\" : \"" + this.workout.getWorkoutName() + "\""
						+ "\"json\" : " + this.workout.getWorkoutJSONString() 
						+ "}";
				
				
				String email = "ale.demurtas92@gmail.com";
				  
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				intent.putExtra(Intent.EXTRA_SUBJECT, "Oggetto");
				intent.putExtra(Intent.EXTRA_TEXT, "Testo");
				 
				File root = Environment.getExternalStorageDirectory();
				  
				File file = new File(root, this.workout.getWorkoutName() + ".stc");
              	
				 try{
					  FileOutputStream out = new FileOutputStream(file);
					  out.write(jsonString.getBytes());
				      out.close();
				  }catch(Exception e){
					  
				  }
				  
				  if (!file.exists()) {
				      Toast.makeText(this.getApplicationContext(), "Attachment Error exist", Toast.LENGTH_SHORT).show();
				      this.finish();
				      return;
				  }
              	  
              	  if(!file.canRead()){
              		  Toast.makeText(this.getApplicationContext(), "Attachment Error read", Toast.LENGTH_SHORT).show();
              		this.finish();
              	      return;
          		  }
              	  
              	  Uri uri = Uri.parse("file://" + file);
              	  intent.putExtra(Intent.EXTRA_STREAM, uri);
              	  this.startActivity(Intent.createChooser(intent, "Invia email..."));
              	  
			break;
			
			/*
			 * If the statistics button is pressed, the workout statistics are shown in 
			 * the appropriate activity.
			 */
			case R.id.btnGraphicEndWorkout:
				i = new Intent(this, WorkoutStatisticsActivity.class);
				startActivity(i); 
			break;
			
			/*
			 * If the exit without save button is pressed, the user goes back to the main 
			 * menu activity and no data is stored on the db.
			 */
			case R.id.btnExitNoSaveEndWorkout:
				this.finish();
			break;
			
			/*
			 * If the exit AND save button is pressed, the user goes back to the main menu activity,
			 * workout statistics are saved on the DB and user general statistics are updated.
			 */
			case R.id.btnExitSaveEndWorkout:
				DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
				dbAdapter.open();
				
				dbAdapter.createNewWorkout(this.workout.getWorkoutName(), this.workout.getWorkoutJSONString());
				
				dbAdapter.close();
				
				this.finish();
			break;
			
		}
		
		
	}
	
}
