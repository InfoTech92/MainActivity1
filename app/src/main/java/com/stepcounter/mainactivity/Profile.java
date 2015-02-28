package com.stepcounter.mainactivity;


/**
 * This class represents a user profile.
 */
public class Profile {
	
	
	/**
	 * A user's default step length of 50 cm.
	 */
	public static final int DEFAULT_STEP_LEN = 50;
	
	/**
	 * User id.
	 */
	private int id;
	
	/**
	 * The user's name.
	 */
	private String name;
	
	/**
	 * The user's surname.
	 */
	private String surname;
	
	/**
	 * The user's step length, in centimeters.
	 */
	private int stepLen;
	
	/**
	 * User's height, in centimeters.
	 */
	private int height;
	
	/**
	 * User's weight, in kg.
	 */
	private int weight;
	
	/**
	 * User's gender. "M" or "F"
	 */
	private String gender; 
	
	
	/**
	 *	The X value.
	 */
	private int x;
	
	/**
	 * Total distance gone by the user, in km.
	 */
	private int totalDistanceKm;
	
	/**
	 * Total number of steps taken by the user.
	 */
	private int stepsTot;
	
	/**
	 * Total training time for this user.
	 */
	private int totalTimeSec;
	
	/**
	 * Total kcal consumed by the user.
	 */
	private double totalKcal;
	
	/**
	 * The path to the user's avatar path.
	 */
	private String avatarPath;
	
	/**
	 * An empty constructor.
	 */
	public Profile(){
	}
	
	
	/**
	 * Sets the user's id.
	 * @param id
	 */
	public void setId(int id){
		this.id = id;
	}
	
	
	/**
	 * Sets the user's name.
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Sets the user's surname.
	 * @param surname
	 */
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	/**
	 * Sets the user's step length, in centimeters.
	 * @param stepLen
	 */
	public void setStepLen(int stepLen){
		this.stepLen = stepLen;
	}
	
	
	
	/**
	 * Sets the user's height, in centimeters.
	 * @param height
	 */
	public void setHeight(int height){
		this.height = height;
	}
	
	
	
	/**
	 * Sets the user's weight, in kg.
	 * @param weight
	 */
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	
	/**
	 * Sets the user's gender. "M" or "F"
	 * @param gender
	 */
	public void setGender(String gender){
		this.gender = gender;
	}
	
	/**
	 * Sets the X value.
	 * @param X
	 */
	public void setX(int x){
		this.x = x;
	}
	
	
	
	
	
	/**
	 * Sets the total distance gon by the user, in km.
	 * @param totalDistanceKm
	 */
	public void setTotalDistanceKm(int totalDistanceKm){
		this.totalDistanceKm = totalDistanceKm;
	}
	
	/**
	 * Sets the total number of steps taken by the user.
	 * @param stepsTot
	 */
	public void setStepsTot(int stepsTot){
		this.stepsTot = stepsTot;
	}
	
	/**
	 * Sets the total training time for the user,
	 * in seconds.
	 * @param totalTimeSec
	 */
	public void setTotalTimeSec(int totalTimeSec){
		this.totalTimeSec = totalTimeSec;
	}
	
	/**
	 * Set the total number of consumed kcals for the user.
	 * @param totalKcal
	 */
	public void setTotalKcal(double totalKcal){
		this.totalKcal = totalKcal;
	}
	
	/**
	 * Set the user's avatar path.
	 * @param avatarPath
	 */
	public void setAvatarPath(String avatarPath){
		this.avatarPath = avatarPath;
	}
	
	
	
	
	
	
	/**
	 * Fetches the user's id.
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	
	
	/**
	 * Fetches the user's name.
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Fetches the user's surname.
	 * @return
	 */
	public String getSurname(){
		return this.surname;
	}
	
	/**
	 * Fetches the user's step length, in centimeters.
	 * @return
	 */
	public int getStepLen(){
		return this.stepLen;
	}
	
	
	
	
	
	/**
	 * Fetches the user's height, in centimeters.
	 * @return 
	 */
	public int getHeight(){
		return this.height;
	}
	
	
	
	/**
	 * Fetches the user's weight, in kg.
	 * @return 
	 */
	public int getWeight(){
		return this.weight;
	}
	
	
	/**
	 * Fetches the user's gender, "M" or "F".
	 * @return 
	 */
	public String getGender(){
		return this.gender;
	}
	
	
	/**
	 * Fetches the x value.
	 * @return
	 */
	public int getX(){
		return this.x;
	}
	
	
	
	
	/**
	 * Fetches the total distance gon by the user, in km.
	 * @return
	 */
	public int getTotalDistanceKm(){
		return this.totalDistanceKm;
	}
	
	/**
	 * Fetches the total number of steps taken by the user.
	 * @return 
	 */
	public int getStepsTot(){
		return this.stepsTot;
	}
	
	/**
	 * Fetches the total training time for the user,
	 * in seconds.
	 * @return 
	 */
	public int getTotalTimeSec(){
		return this.totalTimeSec;
	}
	
	/**
	 * Fetches the total number of consumed kcals for the user.
	 * @return 
	 */
	public double getTotalKcal(){
		return this.totalKcal;
	}
	
	/**
	 * Fetches the user's avatar path.
	 * @return
	 */
	public String getAvatarPath(){
		return this.avatarPath;
	}
	
	
}
