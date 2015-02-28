package com.stepcounter.mainactivity;

import com.example.mainactivity.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * This class represents the activity which is launched when the user tries to
 * make a new training but he has not inserted his personal data.
 *
 */
public class ErrorActivity extends ActionBarActivity implements OnClickListener{
	
	
	/**
	 * When the activity is created, this method is launched.
	 * 
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    
        Button btnProfileManagementErrorActivity = (Button)findViewById(R.id.btnProfileManagementErrorActivity);
        Button btnCancelErrorActivity = (Button)findViewById(R.id.btnCancelErrorActivity);
        
        btnProfileManagementErrorActivity.setOnClickListener(this);
        btnCancelErrorActivity.setOnClickListener(this);
        
        
        
    }
	
    
    /**
     * OnClickListener method implementation.
     */
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.btnProfileManagementErrorActivity:
				Intent i = new Intent(this, ProfileManagementActivity.class);
				startActivity(i);
				this.finish();
			break;
		
			case R.id.btnCancelErrorActivity:
				this.finish();
			break;
		
		}
		
		
	}
	
}
