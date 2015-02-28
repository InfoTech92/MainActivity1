package com.stepcounter.mainactivity;

import com.example.mainactivity.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * This class represents the application's main activity.
 * It is the first activity shown when the application is launched.
 * 
 *
 */
public class MainActivity extends ActionBarActivity implements OnClickListener{
	
	/**
	 * This field is a reference to the object itself.
	 */
	private MainActivity self = null;
	
	
	
	
	/**
	 * A boolean field which indicates if a user has been found in the database.
	 */
	private boolean userFound;
	
	
	
	private Profile user;
	
	/**
	 * When the activity is created, this method is launched.
	 * 
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //Buttons initialization
        Button btnNewWorkout = (Button) findViewById(R.id.btnNewWorkout);
        Button btnViewWorkouts = (Button)findViewById(R.id.btnViewWorkouts);
        Button btnProfileManagement = (Button)findViewById(R.id.btnProfileManagement);
        
        btnNewWorkout.setOnClickListener(this); 
        btnViewWorkouts.setOnClickListener(this);
        btnProfileManagement.setOnClickListener(this);
        
        
        //Self variable assigned
        self = this;
        
        
    }
    
    
    
    
    public void onResume(){
    	super.onResume();
    	
    	this.user = new Profile();
    	
    	//A database adapter: this is useful to know the user list in the database.
        DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        
        Cursor profiles = dbAdapter.fetchAllProfiles();
        
        this.userFound = profiles.moveToFirst();
        if(this.userFound){
        	this.user.setStepLen(profiles.getInt(profiles.getColumnIndex("step_len_cm")));
        }
        
        dbAdapter.close();
        
    }
    
    
    /**
     * OnClickListener interface method.
     */
	public void onClick(View v) {
		
		Intent i = null;
		
		switch(v.getId()){
		
			/*
			 * When the new workout button is touched, a pop up is created (if it doesn't already exist)
			 * that is necessary to initialize workout basic data (name and step length).
			 * If no user is registered in the DB, the ErrorActivity is launched.
			 */
			case R.id.btnNewWorkout:
				
				
				
				if(this.userFound){
					
	            	NewWorkoutPopup.createNewWorkoutPopup(this, this.user);
	                
				}else{
					
					i = new Intent(self, ErrorActivity.class);
    				startActivity(i);
				}
				
			break;
			
			
			//If the "View Workouts" button is touched, ViewWorkoutActivity (workout list) is started.
			case R.id.btnViewWorkouts:
				i = new Intent(this, ViewWorkoutsActivity.class);
				startActivity(i); 
			break;
			
			
			//When the profile button is touched, ProfileManagementActivity is started.
			case R.id.btnProfileManagement:
				i = new Intent(this, ProfileManagementActivity.class);
				startActivity(i); 
			break;
			
		}
		
		
		
	}
	
	
	
	

	/**
	 * Creates the options menu.
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
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
	
	
	
	
    
}
