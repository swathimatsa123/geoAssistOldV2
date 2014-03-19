package com.geoassist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
//public class syncTab extends Fragment {
public class syncTab extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Float azimut; 
    float[] mGravity;
    float[] mGeomagnetic;
    Sensor accelerometer = null;
    Sensor magnetometer = null;
    compassView cmpsView = null; 
    private static long   sensorCount = 0;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sync_tab,container, false);
//		cmpsView = new compassView(getActivity());
//		getActivity().setContentView(cmpsView);
        return rootView;
	}
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
			accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(getActivity(), "Accelero Meter is not available", Toast.LENGTH_LONG).show();
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
			magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(getActivity(), "Magnetic Sensor is not available", Toast.LENGTH_LONG).show();
		}
	}
	

	@Override
	public void onPause(){
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		String dispStr = "";
		switch (accuracy) {
		case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
			dispStr = "High";
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
			dispStr = "Low";
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
			dispStr = "Medium";
			break;
		case SensorManager.SENSOR_STATUS_UNRELIABLE:	
			dispStr = "Unreliable";
			break;
		}
		Toast.makeText(getActivity(), "Sensor Acuuracy is" + dispStr, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		sensorCount = sensorCount+1;
		if (sensorCount == 200) sensorCount = 0;
		else {
			return;
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	        mGravity = event.values;
	    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
	        mGeomagnetic = event.values;
	    if (mGravity != null && mGeomagnetic != null) {
	        float R[] = new float[9];
	        float I[] = new float[9];
	        boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
	        if (success) {
	          float orientation[] = new float[3];
	          SensorManager.getOrientation(R, orientation);
	          azimut = orientation[0]; // orientation contains: azimut, pitch and roll
//	          Log.e("Sensor Values" , " A: "+String.valueOf(orientation[0])+
//	        		  				  " X: "+String.valueOf(orientation[1])+
//	        		  				  " Y: "+String.valueOf(orientation[2]));
	          double xd = 0; 
	          double yd = 0; 
	          double zd = 0;
//	          zd = Math.toDegrees(Math.abs(orientation[0]));
//	          xd = Math.toDegrees(Math.abs(orientation[1]));
//	          yd = Math.toDegrees(Math.abs(orientation[2]));
	          zd = Math.toDegrees(orientation[0]);
	          xd = Math.toDegrees(orientation[1]);
	          yd = Math.toDegrees(orientation[2]);

//	          Log.e("Degree Values" , " Z: "+String.valueOf(zd)+
//	  				  " X: "+String.valueOf(xd)+
//	  				  " Y: "+String.valueOf(yd));
	          if (this.cmpsView != null){
	        	  this.cmpsView.update(azimut, 0, R, orientation[0],orientation[1], orientation[2]);
	          }
	        }
	    	  
	    }
	}
}
