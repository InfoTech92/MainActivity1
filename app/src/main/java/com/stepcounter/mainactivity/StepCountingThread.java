package com.stepcounter.mainactivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
 

 
/**
 * This class creates a new thread in which the accelerometer data
 * is processed to know the values of interest. 
 * 
 * @author Created by Saeid on 22-4-2014.
 * @author Alessio |  (modifications)
 */
final class StepCountingThread implements Runnable {
	
	/**
	 * This thread's context.
	 */
    private Context mContext;
    
    /**
     * The sensor manager used for connecting the accelerometer.
     */
    private SensorManager mSensorManager;
    
    /**
     * The accelerometer sensor.
     */
    private Sensor mSensor;
    
    /**
     * The step counting library that calculates the values (speed, steps/min ecc)
     * and sends them to the main thread.
     */
    public StepCountingLibrary mListener;
    
    /**
     * The handler thread for the sensor manager.
     */
    private HandlerThread mHandlerThread;
    
    /**
     * The handler that is used to notify the main thread that new data is available.
     */
    public Handler handler;
    
    /**
     * The delay time between one accelerometer sample and another.
     */
    public static final int DELAY = 500000;
    
    /**
     * The user's step length, in centimeters.
     */
    public int stepLen;
    
    /**
     * 
     * The constructor: initializes the basic data.
     * 
     * @param context
     * @param hand
     * @param stepLen
     */
    public StepCountingThread(Context context, Handler hand, int stepLen) {

    	this.mContext = context;
    	this.handler = hand;
    	this.stepLen = stepLen;
    	
    }
 
    
    /**
     * Method to be run in the newly created thread.
     */
    @Override
    public void run() {
    	
    	//The accelerometer sensor is taken from the sensor manager object.
        this.mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       
        //The handler thread for the sensor.
        this.mHandlerThread = new HandlerThread("AccelerometerLogListener");
        this.mHandlerThread.start();
        
        Handler handlerThread = new Handler(this.mHandlerThread.getLooper());
        
        //The StepCountingLibrary is here created and executed.
        this.mListener = new StepCountingLibrary(this.handler, this.stepLen);
        
        //The listener is registered to the accelerometer events.
        this.mSensorManager.registerListener(this.mListener, this.mSensor, DELAY , handlerThread);
    	
    }
 
 
}