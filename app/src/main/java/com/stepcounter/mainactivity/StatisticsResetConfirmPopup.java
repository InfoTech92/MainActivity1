package com.stepcounter.mainactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mainactivity.R;

/**
 * This activity contains a static method that creates the workout elimination pop up:
 * the pop up is created when the user touches on the "New Workout" button 
 * and asks the user for the workout name and the desired mean speed in 
 * steps/minutes.
 * 
 * This class has no constructors.
 * 
 */
public class StatisticsResetConfirmPopup{
	
	/**
	 * This object represents the layout of the pop up.
	 */
	private static RelativeLayout layout;
	
	/**
	 * This object is used for pop up creation.
	 */
	private static LayoutInflater layoutInflater;
	
	/**
	 * This object is a View that contains the pop up.
	 */
	private static View popupView;
	
	/**
	 * This object represents the activity in which the pop up
	 * must be created.
	 */
	private static ProfileManagementActivity activ;
	
	/**
	 * This field represents the new pop up.
	 */
	private static PopupWindow popupWindow = null;
	

	/**
	 * The user profile to reset.
	 */
	private static Profile userProfile;
	
	/**
	 * This method creates a new pop up..
	 * 
	 */
	public static void createStatisticsResetConfirmPopup(ProfileManagementActivity activity, Profile user){
		
		//If a pop up already existed, it is here deleted: 
		//only a pop up can exist at any time.
		dismissPopup();
		
		//The parameter activity is transferred as a local field.
		activ = activity;
		
		/*
		 * Pop up creation code.
		 */
		
		//The pop up layout is fetched. This resource is found within the
		//new_workout_popup.xml file
		layout = (RelativeLayout) activity.findViewById(R.id.statistics_reset_confirm_popup);
		//The LayoutInflater service is created using the getSystemService method.
		layoutInflater = (LayoutInflater)activity.getBaseContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  
		//The pop up view is created by the layoutInflater. 
		popupView = layoutInflater.inflate(R.layout.popup_statistics_reset_confirm, layout);
		
		//The pop up window is created from the pop up view.
		popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
		
		//The window is declared to be focusable (this is necessary for the input).
		popupWindow.setFocusable(true);
		//The pop up window is finally shown.
		popupWindow.showAsDropDown(activity.findViewById(R.id.profileManagementActivityGenderTextView));
		
		userProfile = user;
	    
		
	    /*
	     * On click listener methods.
	     */
	    
	    Button statisticsResetConfirmPopupCancelButton = (Button) popupView.findViewById(R.id.statisticsResetConfirmPopupCancelButton);
	    statisticsResetConfirmPopupCancelButton.setOnClickListener(new Button.OnClickListener(){
		
			@Override
			public void onClick(View v) {
				
				dismissPopup();
				
		    }
			
	    });
		
		
		Button statisticsResetConfirmPopupConfirmButton = (Button) popupView.findViewById(R.id.statisticsResetConfirmPopupConfirmButton);
		statisticsResetConfirmPopupConfirmButton.setOnClickListener(new Button.OnClickListener(){
		
			@Override
			public void onClick(View v) {
				
				String name 	  = userProfile.getName();
			   	String surname	  = userProfile.getSurname();
			   	String gender 	  = userProfile.getGender();
				int height 		  = userProfile.getHeight();
				int weight 		  = userProfile.getWeight();
				int stepLen 	  = userProfile.getStepLen();
				int x 			  = userProfile.getX();
				String avatarPath = userProfile.getAvatarPath();
				
				int totalDistanceKm = 0;
				int stepsTot = 0;
				int totalTimeSec = 0;
				int kcalTot = 0;
				
				long id = userProfile.getId();
				
					
				DatabaseAdapter dbAdapter = new DatabaseAdapter(activ.getApplicationContext());
				dbAdapter.open();
				
				dbAdapter.updateProfile(id, name, surname, gender, height, weight, stepLen, totalDistanceKm, stepsTot, totalTimeSec, kcalTot, x, avatarPath);
				
				dbAdapter.close();
				
				dismissPopup();
				
				activ.fetchUserData();
				activ.initializeTextFields();
				
				Toast.makeText(activ , "Statistiche azzarate con successo!",
						Toast.LENGTH_LONG).show();
		    }
			
	    });
		
	    
	
    
    
	}
    
    
	
	
	
	/**
	 * This function removes the pop up, if it doesn't exist.
	 */
	private static void dismissPopup(){
		if(popupWindow != null){
			popupWindow.dismiss();
    	}
	}
	
}
