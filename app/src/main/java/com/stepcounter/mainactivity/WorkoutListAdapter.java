package com.stepcounter.mainactivity;



import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.example.mainactivity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is useful to handle the workout lists which are shown in the
 * ViewWorkoutsActivity.
 *
 */
public class WorkoutListAdapter extends ArrayAdapter<Workout> {
	
	
	/**
	 * Application context.
	 */
	public Context context;
	/**
	 * Layout resource id field.
	 */
	public int layoutResourceId;
	
	/**
	 * Workouts ArrayList.
	 */
	public ArrayList<Workout> data = new ArrayList<Workout>();

	/**
	 * Activity that used the workout list adapter.
	 */
	public Activity activity;
	
	
	/**
	 * This object.
	 */
	public WorkoutListAdapter self;
	
	
	
	/**
	 * Send email activity code.
	 */
	public static final int SEND_EMAIL_CODE = 1;
	
	
	
	public File tempFile;
	
	/**
	 * Class constructor.
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param data
	 */
	public WorkoutListAdapter(Activity activ, Context context, int layoutResourceId, ArrayList<Workout> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.activity = activ;
		this.self = this;
	}

	
	
	/**
	 * This method gets a list row and sets the listeners for the
	 * row buttons.
	 * 
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		WorkoutHolder holder = null;

		if (row == null) {
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new WorkoutHolder();
			holder.userNameLabel = (TextView) row.findViewById(R.id.userNameLabel);
			holder.shareSymbolImage = (ImageView) row.findViewById(R.id.shareSymbolImage);
			holder.trashCanSymbolImage = (ImageView) row.findViewById(R.id.trashCanSymbolImage);
			holder.eyeSymbolImage = (ImageView) row.findViewById(R.id.eyeSymbolImage);
			row.setTag(holder);
			
		} else {
			holder = (WorkoutHolder) row.getTag();
		}
		
		
		Workout workout = data.get(position);
		holder.userNameLabel.setText(workout.getWorkoutName());
		
		
		holder.eyeSymbolImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, WorkoutStatisticsActivity.class);
				i.putExtra("workout_id", data.get(position).getWorkoutId());
				i.putExtra("workout_name", data.get(position).getWorkoutName());
				i.putExtra("workout_json", data.get(position).getWorkoutJSONString());
				context.startActivity(i);
			}
		});
		
		
		
		holder.trashCanSymbolImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WorkoutEliminationConfirmPopup.createWorkoutEliminationConfirmPopup(activity, (ImageView) v, position, data, self);;
			}
		});
		
        holder.shareSymbolImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	
				String email = "ale.demurtas92@gmail.com";
				  
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
				intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
				intent.putExtra(Intent.EXTRA_TEXT, "body text");
				 
				File root = Environment.getExternalStorageDirectory();
				  
				File file = new File(root, "FileProva.stc");
              	  
				 try{
					  FileOutputStream out = new FileOutputStream(file);
				      out.write("buon li".getBytes());
				      out.close();
				  }catch(Exception e){
					  
				  }
				  
				  if (!file.exists()) {
				      Toast.makeText(context, "Attachment Error exist", Toast.LENGTH_SHORT).show();
				      activity.finish();
				      return;
				  }
              	  
              	  if(!file.canRead()){
              		  Toast.makeText(context, "Attachment Error read", Toast.LENGTH_SHORT).show();
              		  activity.finish();
              	      return;
          		  }
              	  
              	  
              	  tempFile = file;
              	  
              	  Uri uri = Uri.parse("file://" + file);
              	  intent.putExtra(Intent.EXTRA_STREAM, uri);
              	  activity.startActivityForResult(Intent.createChooser(intent, "Send email..."), SEND_EMAIL_CODE);
              	  
            }
        });

        return row;

	}
	
	
	
	/**
     * onActivityResult implementation. This code is used when startActivityForResult is called. 
     */
    public void onActivityResult(int reqCode, int resCode, Intent data){

        if (resCode == Activity.RESULT_OK) {
            if (reqCode == 1){
            	tempFile.delete();
            }
        }

    }
	
	
	
	
	
	
	
	/**
	 * This class contains fields the represent the views 
	 * in a list row.
	 *
	 */
	static class WorkoutHolder {
		public TextView userNameLabel;
		public ImageView shareSymbolImage;
		public ImageView trashCanSymbolImage;
		public ImageView eyeSymbolImage;
	}
	
	
}














