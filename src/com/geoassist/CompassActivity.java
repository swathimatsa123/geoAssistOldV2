package com.geoassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class CompassActivity extends Activity implements SensorEventListener {
	    private SensorManager mSensorManager;
	    private Float azimut; 
	    float[] mGravity;
	    float[] mGeomagnetic;
	    float declanation = 0;
	    Sensor accelerometer = null;
	    Sensor magnetometer = null;
	    compassView cmpsView = null; 
	    EditText strikeTxt = null;
	    EditText dipTxt = null;
	    EditText allInfoTxt = null;
	    private static long   sensorCount = 0;
	    private static final int COMPASS_CORRECTION = 300;

	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		strikeTxt = (EditText) findViewById(R.id.strikeTitle);
		dipTxt = (EditText) findViewById(R.id.dipTitle);
		allInfoTxt = (EditText) findViewById(R.id.allInfo);

		WindowManager windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
		int rotation = windowManager.getDefaultDisplay().getRotation();
//		this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.setRequestedOrientation (rotation);

	}
	@Override 
	public void onResume () {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
			accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(this, "Accelero Meter is not available", Toast.LENGTH_LONG).show();
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
			magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(this, "Magnetic Sensor is not available", Toast.LENGTH_LONG).show();
		}
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compass_view_menu, menu);
		return true;
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
//		Toast.makeText(this, "Sensor Acuuracy is" + dispStr, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (sensorCount == 50) sensorCount = 0;
		else {
			if (sensorCount != 0)
			{
				sensorCount = sensorCount+1;
				return;
			}
		}
		int mGeomagneticLen = 0;
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	        mGravity = event.values;
	    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	        mGeomagnetic = event.values;
	        mGeomagneticLen = event.values.length;
	    }
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
	          zd  = zd - this.declanation -11;
	          xd = Math.toDegrees(orientation[1]);
	          yd = Math.toDegrees(orientation[2]);
	          yd = (double) Math.round(yd * 100) / 100;
	          zd = (double) Math.round(zd * 100) / 100;
	          xd = (double) Math.round(xd * 100) / 100;
	          
	          String direction = "";
	          if ( (0 < yd) && (yd <90) ){
	        	  direction = "South";
	          }
	          else if ((90 < yd) && (yd <180) ){
	        	  direction = "East";	        	  
	          }
	          else if ((-180 < yd) && (yd < -90) ){
	        	  direction = "North";	        	  
	          }
	          else {
	        	  direction = "West";	        	  
	          }
//	          Log.e("Degree Values" , " Z: "+String.valueOf(zd)+
//	  				  " X: "+String.valueOf(xd)+
//	  				  " Y: "+String.valueOf(yd));
	          if (strikeTxt != null) strikeTxt.setText("Strike  " + String.valueOf(Math.abs(zd)));
	          if (dipTxt != null) dipTxt.setText("Dip    " +String.valueOf(Math.abs(xd)) + "  " + direction);
	          if (allInfoTxt != null) allInfoTxt.setText("Az :" + 
	        		  									 String.valueOf(zd) + 
	        		  									 "  X : " + String.valueOf(xd) +
	        		  									 "  Y : " + String.valueOf(yd));
//	          String dispStr = "";
//	          for (int i=0;i< mGeomagnetic.length;i++){
//	        	dispStr = dispStr+ " : " + String.valueOf(mGeomagnetic[i]);  
//	          }
//	          Log.e("Geo Magnet", dispStr);

	          String dispStr = "";
	          for (int i=0;i< mGravity.length;i++){
	        	dispStr = dispStr+ " : " + String.valueOf(mGravity[i]);  
	          }
//	          Log.e(" Gravity ", dispStr);

//	          if (this.cmpsView != null){
//	        	  this.cmpsView.update(azimut, 0, R, orientation[0],orientation[1], orientation[2]);
//	          }
	        }
	    }
	}
	public void invokeCompassCorrection()
	{
    	Intent mIntent= new Intent(getApplicationContext(),CompassCorrection.class);
    	startActivityForResult(mIntent, COMPASS_CORRECTION);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == COMPASS_CORRECTION) {
		  this.declanation = data.getFloatExtra("declanation", 0);
	  }
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ic_compass_fix:
			invokeCompassCorrection();
        	break;        	
		case R.id.ic_save:
			this.finish();
        	break;        	

		}
		return true;
	}
}
