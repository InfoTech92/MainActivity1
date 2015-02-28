package com.stepcounter.mainactivity;

import org.json.JSONObject;


/**
 * This class contains the fields and the getter/setters methods
 * which are necessary for a workout representation,
 * according with the workouts table in the DB.
 * 
 */
public class Workout {
	
	
	/**
	 * Workout id.
	 */
	private int _id;
	/**
	 * Workout name.
	 */
	private String name;
	/**
	 * Workout JSON object: it contains the main workout statistics and data.
	 */
	private JSONObject json;
	
	
	/**
	 * Constructor.
	 * @param _id
	 * @param name
	 * @param json
	 */
	public Workout(int _id, String name, String json){
		this._id = _id;
		this.name = name;
		this.json = new JSONObject(json);
	}
	
	/**
	 * Constructor.
	 * @param _id
	 * @param name
	 * @param json
	 */
	public Workout(int _id, String name, JSONObject json){
		this._id = _id;
		this.name = name;
		this.json = json;
	}
	
	/**
	 * Returns the workout id.
	 * @return
	 */
	public int getWorkoutId(){
		return this._id;
	}
	/**
	 * Returns the workout name.
	 * @return
	 */
	public String getWorkoutName(){
		return this.name;
	}
	
	/**
	 * Returns the workout JSON as a String.
	 * @return
	 */
	public String getWorkoutJSONString(){
		return this.json.toString();
	}
	
	/**
	 * Returns the workout JSON as a JSONObject.
	 * @return
	 */
	public JSONObject getWorkoutJSON(){
		return this.json;
	}
	
	
	
	
	
	
	/**
	 * Sets the workout id.
	 * @param _id
	 */
	public void setWorkoutId(int _id){
		this._id = _id;
	}
	
	/**
	 * Sets the workout name.
	 * @param name
	 */
	public void setWorkoutName(String name){
		this.name = name;
	}
	
	/**
	 * Sets the workout JSON object by using a String.
	 * @param json
	 */
	public void setWorkoutJSON(String json){
		this.json = new JSONObject(json);
	}
	
	/**
	 * Sets the workout JSON object by using a JSONObject.
	 * @param json
	 */
	public void setWorkoutJSON(JSONObject json){
		this.json = json;
	}
	
	
}
