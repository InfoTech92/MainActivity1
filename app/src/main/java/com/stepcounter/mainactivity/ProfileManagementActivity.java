package com.stepcounter.mainactivity;

import com.example.mainactivity.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * This is the profile management activity which is used to handle personal data such as
 * name, surname and the other fields shown in the activity itself.
 *
 */
public class ProfileManagementActivity extends ActionBarActivity implements OnClickListener{
	
	/**
	 * A DB adapter is useful to handle the database and search for user data.
	 */
	private DatabaseAdapter dbAdapter;
	
	/**
	 * The avater image.
	 */
    ImageView avatar;
    
    /**
     * The reset image.
     */
    ImageView reset;
    
    EditText profileManagementActivityNameEditText;
    EditText profileManagementActivitySurnameEditText;
    EditText profileManagementActivityHeightEditText;
    EditText profileManagementActivityWeightEditText;
    EditText profileManagementActivityStepLengthEditText;
    EditText profileManagementActivityGenderEditText;
    EditText profileManagementActivityXEditText;
	
	private ActionBarActivity self;
    private Profile user;
    private boolean userFound;
    
	/**
	 * Creates the activity.
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        self = this;
        
        setContentView(R.layout.activity_profile_management);

        //Image initialization...
        this.avatar = (ImageView) findViewById(R.id.circularImage);

        //Implementation of startActivityForResult
        this.avatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            	if(userFound){
	                Intent intent  = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), 1);
            	}else{
            		Toast.makeText(self , "Prima di inserire un avatar, inserire i propri dati e toccare su \"Salva\"",
        					Toast.LENGTH_LONG).show();
            	}
            }
        } );
        
        
        
        

        this.profileManagementActivityNameEditText 		 = (EditText) findViewById(R.id.profileManagementActivityNameEditText);
        this.profileManagementActivitySurnameEditText 	 = (EditText) findViewById(R.id.profileManagementActivitySurnameEditText);
        this.profileManagementActivityHeightEditText 	 = (EditText) findViewById(R.id.profileManagementActivityHeightEditText);
        this.profileManagementActivityWeightEditText 	 = (EditText) findViewById(R.id.profileManagementActivityWeightEditText);
        this.profileManagementActivityStepLengthEditText = (EditText) findViewById(R.id.profileManagementActivityStepLengthEditText);
        this.profileManagementActivityGenderEditText   	 = (EditText) findViewById(R.id.profileManagementActivityGenderEditText);
        this.profileManagementActivityXEditText 		 = (EditText) findViewById(R.id.profileManagementActivityXEditText);
        
        
        
        
    	
    	
        
        
        this.reset = (ImageView) findViewById(R.id.circularImage2);
        this.reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               
            	resetData();

            }
        } );
        
        
        
        fetchUserData();
        
        //Text fields initialization.
        if(this.userFound){
	       this.initializeTextFields();
        }
        
        
        
        Button btnProfileManagementActivitySave = (Button) findViewById(R.id.btnProfileManagementActivitySave);
        btnProfileManagementActivitySave.setOnClickListener(this);
        
        
	}

	/**
	 * OnClickListenere interface implementation.
	 */
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			
			case R.id.btnProfileManagementActivitySave:{
				
				this.saveData();
				break;
			}
			
		}
	
		
	}

    /**
     * onActivityResult implementation. This code is used when is called startActivityForResult
     */
    public void onActivityResult(int reqCode, int resCode, Intent data){

        if (resCode == RESULT_OK) {
            if (reqCode == 1){
                this.avatar.setImageURI(data.getData());
                this.user.setAvatarPath(data.getData().toString());
                updateAvatarPath();
            }
        }

    }
	
    
    
    
    
    private void resetData(){
    	fetchUserData();
    	
    	if(this.user != null){
    		StatisticsResetConfirmPopup.createStatisticsResetConfirmPopup(this, this.user);
    	}else{
    		Toast.makeText(this , "Nessun utente trovato: impossibile azzerare le statistiche!",
					Toast.LENGTH_LONG).show();
    	}
	   
    }
    
    
    
    private void updateAvatarPath(){
    	
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
    
    
		this.dbAdapter = new DatabaseAdapter(this.getApplicationContext());
		this.dbAdapter.open();
		
		if(! this.userFound){
			this.dbAdapter.createNewProfile(name, surname, gender, height, weight, stepLen, 0, 0, 0, 0, x, avatarPath);
		}else{
			this.dbAdapter.updateProfile(id, name, surname, gender, height, weight, stepLen, totalDistanceKm, stepsTot, totalTimeSec, kcalTot, x, avatarPath);
		}
			
		this.dbAdapter.close();
		
		
    }
    
    
    
    
    
    
    
	
   /**
    * This function saves the data on the database.
    */
   private void saveData(){
	   
	   String name 			 = this.profileManagementActivityNameEditText.getText().toString();
	   String surname		 = this.profileManagementActivitySurnameEditText.getText().toString();
	   String gender 		 = this.profileManagementActivityGenderEditText.getText().toString();
	   String heightString 	 = this.profileManagementActivityHeightEditText.getText().toString();
	   String weightString 	 = this.profileManagementActivityWeightEditText.getText().toString();
	   String stepLenString  = this.profileManagementActivityStepLengthEditText.getText().toString();
	   String xString 		 = this.profileManagementActivityXEditText.getText().toString();
	   
	   if(! name.equalsIgnoreCase("") && ! surname.equalsIgnoreCase("") && ! gender.equalsIgnoreCase("") &&
			   ! heightString.equalsIgnoreCase("") && ! weightString.equalsIgnoreCase("") && ! stepLenString.equalsIgnoreCase("") &&
			   ! xString.equalsIgnoreCase("")){
		   
		   	
			int height 	= Integer.parseInt(heightString);
			int weight 	= Integer.parseInt(weightString);
			int stepLen = Integer.parseInt(stepLenString);
			int x 		= Integer.parseInt(xString);
			
			int totalDistanceKm  = 0;
			int stepsTot 		 = 0;
			int totalTimeSec 	 = 0;
			double kcalTot 		 = 0;
			long id 			 = 0;
			String avatarPath = null;
			
			if(this.userFound){
				totalDistanceKm   = this.user.getTotalDistanceKm();
				stepsTot 		  = this.user.getStepsTot();
				totalTimeSec 	  = this.user.getTotalTimeSec();
				kcalTot 		  = this.user.getTotalKcal();
				id 			 	  = this.user.getId();
				avatarPath 		  = this.user.getAvatarPath();
			}
			
			
			if(avatarPath == null){
				avatarPath = "";
			}
			
			if( height >= 0 && weight  >= 0 && stepLen >= 0 && x >= 0){
			
				this.dbAdapter = new DatabaseAdapter(this.getApplicationContext());
				this.dbAdapter.open();
				
				if(! this.userFound){
					this.dbAdapter.createNewProfile(name, surname, gender, height, weight, stepLen, 0, 0, 0, 0, x, avatarPath);
				}else{
					this.dbAdapter.updateProfile(id, name, surname, gender, height, weight, stepLen, totalDistanceKm, stepsTot, totalTimeSec, kcalTot, x, avatarPath);
				}
					
				this.dbAdapter.close();
				
				Toast.makeText(this , "Dati Salvati con successo!",
						Toast.LENGTH_LONG).show();
				
			}else{
				Toast.makeText(this , "Errore! I valori numerici devono essere positivi!",
						Toast.LENGTH_LONG).show();
			}
			
		}else{
			Toast.makeText(this , "Errore! Compilare tutti i campi e toccare \"Salva\".",
					Toast.LENGTH_LONG).show();
		}
	   
	   
   }
	
	
    @Override
    public void onDestroy(){
    	
    	this.saveData();
    	
    	super.onDestroy();
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
	
	
    
    
    public void initializeTextFields(){
    	if(! this.user.getAvatarPath().equalsIgnoreCase("")){
    		this.avatar.setImageURI(Uri.parse(this.user.getAvatarPath()));
    	}
    	
    	this.profileManagementActivityNameEditText.setText(this.user.getName());
    	this.profileManagementActivitySurnameEditText.setText(this.user.getSurname());
    	this.profileManagementActivityHeightEditText.setText(Integer.toString(this.user.getHeight()));
    	this.profileManagementActivityWeightEditText.setText(Integer.toString(this.user.getWeight()));
    	this.profileManagementActivityStepLengthEditText.setText(Integer.toString(this.user.getStepLen()));
    	this.profileManagementActivityGenderEditText.setText(this.user.getGender());
    	this.profileManagementActivityXEditText.setText(Integer.toString(this.user.getX()));
    }
	
	
}
