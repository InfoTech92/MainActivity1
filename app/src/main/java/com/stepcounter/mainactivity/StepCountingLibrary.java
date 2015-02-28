package com.stepcounter.mainactivity;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;


/**
 * This class contains all the methods that are necessary for calculate the
 * erquired values.
 *
 */
public class StepCountingLibrary implements SensorEventListener {
	
	/**
	 * This constant indicates that new data is now available for the main 
	 * thread to pick.
	 */
	public static final int NEW_DATA_AVAILABLE = 1;
	
	
	/**
	 * Common data lock.
	 */
	public boolean lock;
	
	/**
	 * This variable indicates if the user started walking.
	 */
	public boolean begin;
	
	/**
	 * This is the number of the accelerometer current sampling.
	 */
	public int i;
	
	/**
	 * This is the sampling number of the last step detected.
	 */
	public int k;
	
	/**
	 * this indicates the maximum value of the acceleration during the current step interval.
	 */
	public double max;
	
	/**
	 * This is the number of steps detected.
	 */
	public int steps;
	
	/**
	 * This is the first parameter of the threshold formula.
	 */
	public double a;
	
	/**
	 * This is the second parameter of the threshold formula.
	 */
	public double b;
	
	/**
	 * This is the threshold variable.
	 */
	public double th;
	
	/**
	 * This ArrayList contains the acceleration samplings.
	 */
	public ArrayList<Float[]> accelerations;
	
	/**
	 * This ArrayList contains the speed samplings over the walking time.
	 */
	public ArrayList<Float> speeds;
	
	/**
	 * This value contains the motions measured by the accelerometer over the 
	 * horizontal axis.
	 */
	public ArrayList<Float> motionsAccelerometer;
	
	/**
	 * This handler is used to communicate the presence of new data 
	 * to the main thread.
	 */
	public Handler handler;
	
	/**
	 * This variable indicates if the step counting algorithm is running
	 * or not.
	 */
	public boolean running;

	/**
	 * This is the total distance calculated by the accelerometer.
	 */
	public float totalDistanceAccelerometer;
	
	/**
	 * This is the total distance calculated by multiplying the number of steps 
	 * for the single step length.
	 */
	public float totalDistanceStepLength;
	
	/**
	 * This ArrayList contains the time samplings.
	 */
	public ArrayList<Integer> timeSamplings;
	
	/**
	 * This is the length of a single step.
	 */
	public int stepLen;
	
	/**
	 * This ArrayList contains, for each step, the number of time intervals
	 * from the previous step.
	 */
	ArrayList<Integer> stepsTime;
	
	/**
	 * This is the number of microseconds from the begin of the training. 
	 */
	int trainingTime;
	
	/**
	 * This is the current speed in meters per second.
	 */
	float currentSpeedMetersPerSecond;
	
	/**
	 * This is the current speed in steps per minute.
	 */
	float currentSpeedStepsPerMinute;
	
	
	/**
	 * Speed samplings in steps per minute.
	 */
	public ArrayList<Float> speedStepsPerMinuteSamplings;
	
	/**
	 * This is the mean speed in meters per seconds.
	 */
	float meanSpeed;
	
	/**
	 * This is the mean speed calculated over the last X meters in meters per seconds.
	 */
	float meanSpeedX;
	
	
	
	/**
	 * This is the sum of the speeds calculated over the time interval.
	 * Used to calculate the mean speed.
	 */
	float speedsSum;
	
	/**
	 * The user's weight.
	 */
	float weight;
	
	/**
	 * Number of kcal's consumed during this workout.
	 */
	double kcal;
	
	/**
	 * This is the initial time sample that must be taken into account when calculating the
	 * mean distance over the last X meters.
	 */
	int initialCamp;
	
	/**
	 * This is the number of time samplings during which the user has
	 * gone the last X meters.
	 */
	int samplingsSum;
	
	/**
	 * This is the number of steps corresponding to the last X meters (X / stepLen).
	 */
	int stepsNum; 
	
	/**
	 * The meters that must be taken into account when the "last X meters"
	 * calculations are done. This value is in meters.
	 */
	int X;
	
	/**
	 * Leg elevation speed samplings.
	 */
	public ArrayList<Float> legElevationSpeedSamplings;
	
	/**
	 * Leg elevation samplings.
	 */
	public ArrayList<Float> legElevationSamplings;
	
	
	
	public int totalLegElevation;
	
	
	
	/**
	 * This constructor initializes the values.
	 * @param hand : the handler that is used to communicate with the main thread.
	 * @param stepLen
	 */
	public StepCountingLibrary(Handler hand, int stepLen){
		
		//At the creation of this object, the walking has not begun yet.
		this.begin = false;
		
		//Number of samplings is zero at the creation.
		this.i = 0;
		
		//Number of steps is zero at the creation.
		this.steps = 0;
		
		
		//These two variables are given short names to maintain coherence
		//with the paper in which the step-counting algorithm is explained.
		
		//The first parameter of the threshold formula (tan(15°)).
		this.a = 0.2679;
		
		//the second parameter of the threshold formula (near pi).
		this.b = 3.1541;
		
		//The handler.
		this.handler = hand;
		
		//Total distance calculated by the accelerometer.
		//(Double integrating the acceleration using the rectangles formula)
		this.totalDistanceAccelerometer = 0;
		
		///Total distance calculated using the step length.
		this.totalDistanceStepLength = 0;
		
		/*
		 * Accelerations, speeds and horizontal motions samplings.
		 * 
		 * Acceleration: m/s2
		 * Speed: m/s
		 * Motions: m
		 */
		this.accelerations = new ArrayList<Float[]>();
		this.speeds = new ArrayList<Float>();
		this.motionsAccelerometer = new ArrayList<Float>();
		
		//Time samplings (one for each time interval).
		this.timeSamplings = new ArrayList<Integer>();
		
		//Time at which the steps have been detected (number of time intervals
		//form the previuos step).
		this.stepsTime = new ArrayList<Integer>();
		
		//Mean speed for the training.
		this.meanSpeed = 0;
		
		//Sum of the calculated speeds (needed for calculating the mean speed).
		//It is in meters per seconds.
		this.speedsSum = 0;
		
		//The first time sampling that is taken into account when doing the
		//"last X meters calculations".
		this.initialCamp = 0;
		
		//Length of a step
		this.stepLen = stepLen;
		
		//The number of steps needed to go X meters forward.
		this.stepsNum = this.X * 100 / stepLen;
		
		//The leg elevation samplings ArrayList is initialized.
		this.legElevationSamplings = new ArrayList<Float>();
		
		//The array list which will hold the speed samplings in
		//steps per minute.
		this.speedStepsPerMinuteSamplings = new ArrayList<Float>();
		
		//Indicates if the step counting is active or paused.
		this.running = false;
		
		this.readFlag = true;
		
		
		this.totalLegElevation = 0;
	}
	
	
	
	
	/**
	 * The method that calculates the new values when the accelerometer detects a new sample.
	 */
	@Override
    public void onSensorChanged(SensorEvent sensorEvent) {
    	
		//If the step counter is not active, nothing has to be done.
		if(this.running = false){
			return;
		}
		
		//auxiliary variables
		float tempSpeeds  = 0;
		
		float minAcc;
		float maxAcc;
		
		float minSpeed;
		float maxSpeed;
		
		///Accelerometer data registration.
		Float[] tempAcc = new Float[3];
		tempAcc[0] = sensorEvent.values[0];
		tempAcc[1] = sensorEvent.values[1];
		tempAcc[2] = sensorEvent.values[2];
		this.accelerations.add(i, tempAcc);
		
		
		
		//If there are at least two accelerometers samplings, the
		//current distance can be calculated, integrating the acceleration values.
		if(this.i > 0){
			
			///Calculating the speed using the rectangule's formula.
			if(tempAcc[0] < this.accelerations.get(this.i-1)[0]){
				minAcc = tempAcc[0]; 
				maxAcc = this.accelerations.get(this.i-1)[0];
			}else{
				maxAcc = tempAcc[0]; 
				minAcc = this.accelerations.get(this.i-1)[0];
			}
			
			
			tempSpeeds = (maxAcc - minAcc) * StepCountingThread.DELAY/2 + (minAcc) * StepCountingThread.DELAY;
			//The calculated speed is registered.
			this.speeds.add(this.i, tempSpeeds);
			
			
			
			//The leg elevation speed is sampled.
			float minAccLegElevation;
			float maxAccLegElevation;
			float tempSpeedsLegElevation;
			
			///Calculating the speed using the rectangule's formula.
			if(tempAcc[1] < this.accelerations.get(this.i-1)[0]){
				minAccLegElevation = tempAcc[1]; 
				maxAccLegElevation = this.accelerations.get(this.i-1)[1];
			}else{
				maxAccLegElevation = tempAcc[1]; 
				minAccLegElevation = this.accelerations.get(this.i-1)[1];
			}
			
			
			tempSpeedsLegElevation = (maxAccLegElevation - minAccLegElevation) * StepCountingThread.DELAY/2 + (minAccLegElevation) * StepCountingThread.DELAY;
			//The calculated speed is registered.
			this.legElevationSpeedSamplings.add(this.i, tempSpeedsLegElevation);
			
			
			
			
			
			
			
			//The measured speeds are summed up in this variable.
			this.speedsSum += tempSpeeds;
			
			//The current speed in meters per second.
			this.currentSpeedMetersPerSecond = tempSpeeds;
			
			//The current mean speed is updated.
			this.meanSpeed = this.speedsSum / (this.i*StepCountingThread.DELAY);
			
			//If at least 3 samples have been taken, the movement can be measured using the rectangule's formula.
			if(this.i > 1){
				
				float tempMotions;
				
				
				//Calculating the movement by integrating the speed using the rectangule's formula.
				if(tempSpeeds < this.speeds.get(this.i-1)){
					minSpeed = tempSpeeds; 
					maxSpeed = this.speeds.get(this.i-1);
				}else{
					maxSpeed = tempSpeeds; 
					minSpeed = this.speeds.get(this.i-1);
				}
				
				tempMotions = (maxSpeed - minSpeed) * StepCountingThread.DELAY / 2 + minSpeed * StepCountingThread.DELAY;
				//The current movement is registered in it's array.
				this.motionsAccelerometer.add(this.i, tempMotions);
				
				
				
				//The leg elevations are calculated and sampled
				float maxSpeedLegElevation;
				float minSpeedLegElevation;
				float tempMotionsLegElevation;
				
				//Calculating the movement by integrating the speed using the rectangule's formula.
				if(tempSpeedsLegElevation < this.speeds.get(this.i-1)){
					minSpeedLegElevation = tempSpeedsLegElevation; 
					maxSpeedLegElevation = this.speeds.get(this.i-1);
				}else{
					maxSpeedLegElevation = tempSpeedsLegElevation; 
					minSpeedLegElevation = this.speeds.get(this.i-1);
				}
				
				
				tempMotionsLegElevation = (maxSpeedLegElevation - minSpeedLegElevation) * StepCountingThread.DELAY / 2 + minSpeedLegElevation * StepCountingThread.DELAY;
				//The current movement is registered in it's array.
				this.legElevationSamplings.add(this.i, tempMotionsLegElevation);
				
				this.totalLegElevation += tempMotionsLegElevation/100;
				
				//The total distance is updated.
				this.totalDistanceAccelerometer += tempMotions;
				
			}
			
			//Adding a new time sampling.
			this.timeSamplings.add(this.i, this.timeSamplings.get(i-1) + StepCountingThread.DELAY);
		}else{
			
			//Adding the first time sampling.
			this.timeSamplings.add(i, StepCountingThread.DELAY);
		}
		
		
		//The total training time is here calculated.
		this.trainingTime = this.timeSamplings.get(i);
		
		//The current distance in steps per minute is calculated.
		this.currentSpeedStepsPerMinute = (this.steps/ this.trainingTime)*60;
		
		this.speedStepsPerMinuteSamplings.add(i, Float.valueOf(this.currentSpeedStepsPerMinute));
		
		
		
		this.i++;
		
		
		//If the walking hasn't begin yet, it is considered to begin when the acceleration 
		//over the x axis is >= 0.08 (as proposed in the paper from which the algorithm is taken).
		//The vertical acceleration must be greater than the threshold (this constraint has been 
		//put up by us to avoid the step counting form start when the user was putting the phone in his pocket).
		//The horizontal acceleration is calculated as a variation from g (Gravity Acceleration).
		if( ! this.begin ){
			
			if(sensorEvent.values[0] >= 0.08 && Math.abs(9.81 - sensorEvent.values[1]) >= -0.3 && Math.abs(9.81 - sensorEvent.values[1]) <= 0.3){
				this.begin = true;
				this.k = this.i;
				this.max = sensorEvent.values[0];
			}
			
		}else{
			
			//The step detection threshold is calculated.
			this.th = this.a / (this.i - this.k) + this.b;
			
			//If the difference between the maximum acceleration registered during the 
			//step time and the current acceleration exceeds the threshold,
			//a new step is detected.
			if(this.max - sensorEvent.values[0] >= this.th){
				//The number of steps is incremented.
				this.steps++;
				
				//The number of samplings from the previous step is registered.
				this.stepsTime.add(this.steps - 1, Integer.valueOf(this.i - this.k));
				
				//The number of samplings in the last X meters is updated.
				this.samplingsSum += (this.i - this.k);
				
				//The sampling number of the last step is updated.
				this.k = this.i;
				
				//The maximum acceleration value over the NEXT step time interval is set up.
				this.max = sensorEvent.values[0];
				
				//The total distance calculated using the user's step length is updated.
				this.totalDistanceStepLength += this.stepLen;
				
				//If the number of steps exceeds the quantity of steps needed to
				//go a distance X, the window is "shifted forward".
				if(this.steps > this.stepsNum){
					//The initial step that must be take into account is updated.
					this.initialCamp++;
					
					//The sampling "tail" is removed.
					this.samplingsSum -= this.stepsTime.get(this.initialCamp - 1);
					
					//The mean speed during the last X meters is updated.
					this.meanSpeedX = this.X / (this.samplingsSum * StepCountingThread.DELAY);
					
				}
				
				
				
				
			}else{
				
				//If the step is not detected, the only thing to do is to update
				//the maximum horizontal acceleration, if necessary.
				if(sensorEvent.values[0] > this.max){
					this.max = sensorEvent.values[0];
				}
			}
			
		}
		
		//The number of kcals is updated according to the formula from this website:
		//http://www.my-personaltrainer.it/consumo-calorico-corsa.html
		this.kcal = 0.5 * this.weight * this.totalDistanceStepLength / 1000;
		
		
		this.setOutput(tempSpeeds);
		
		
		//A message to the main thread is sent to indicate that the
		//new values have been calculated.
		this.handler.sendEmptyMessage(NEW_DATA_AVAILABLE);
		
		
    }
	
	
	
	public double currentSpeedKmHOut;
	public int 	stepsNumberOut;
	
	public int 	distanceKmOut;
	public double 	meanSpeedKmHOut;
	public float stepsPerMinuteOut;
	public float meanSpeedLastXOut;
	public float stepsInLastXMetersOut;
	public double totalDistanceOut;
	public boolean readFlag;
	
	public void setOutput(double tempSpeeds){
		
		if(this.readFlag){
			this.readFlag = false;
			this.currentSpeedKmHOut = tempSpeeds * 3.6; 
			this.stepsNumberOut = this.steps;
			this.meanSpeedKmHOut = this.meanSpeed * 3.6;
			this.stepsPerMinuteOut = this.currentSpeedMetersPerSecond;
			this.meanSpeedLastXOut = this.meanSpeedX;
			this.stepsInLastXMetersOut = this.stepsNum;
			this.totalDistanceOut =  ((double) ((this.steps * this.stepLen) / 100)) / 1000.0;
		}
		
	}
	
	
	
	public void stop(){
		this.running = false;
		this.begin = false;
	}
	
	
	public void resume(){
		this.running = true;
		this.begin = false;
	}
	
	
	
	/**
	 * Method override. Actually unused.
	 */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	
    }
    
    
    
}
