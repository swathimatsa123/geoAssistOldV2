package com.geoassist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

//public class viewTab extends Fragment {
public class viewTab extends Fragment implements SensorEventListener {
	// define the display assembly compass picture
	private ImageView image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	TextView tvHeading;
	private float [] mGravity;
	private float [] mGeomagnetic;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_tab,container, false);
		// our compass image
		image = (ImageView) rootView.findViewById(R.id.imageViewCompass);

		// TextView that will tell the user what degree is he heading
		tvHeading = (TextView) rootView.findViewById(R.id.tvHeading);

//		 initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//		mSensorManager.registerListener(this, 
//							mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//							SensorManager.SENSOR_DELAY_GAME);
		
		mSensorManager.registerListener(this, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_UI);
		
		mSensorManager.registerListener(this, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
	            SensorManager.SENSOR_DELAY_UI);		
		
		return rootView;
	}
	@Override
	public void onSensorChanged(SensorEvent event) {

		float playerAngle =0;
		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(
				currentDegree, 
				-degree,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
		currentDegree = -degree;

//		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//			   mGravity = event.values;
//			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//			   mGeomagnetic = event.values;
//			if (mGravity != null && mGeomagnetic != null) {
//			   float R[] = new float[9];
//			   float I[] = new float[9];
//
//			  boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//			   if (success) {
//			       float orientation[] = new float[3];
//			       SensorManager.getOrientation(R, orientation);
//			       playerAngle = (float) Math.toDegrees(orientation[1]);
//			       tvHeading.setText("Heading: " + Float.toString(playerAngle) + " degrees");
//			   }
//		}
//		// create a rotation animation (reverse turn degree degrees)
//		RotateAnimation ra = new RotateAnimation(currentDegree, 
//					-playerAngle,
//					Animation.RELATIVE_TO_SELF, 
//					0.5f, 
//					Animation.RELATIVE_TO_SELF,
//					0.5f);
//	
//		// how long the animation will take place
//		ra.setDuration(210);
//	
//		// set the animation after the end of the reservation status
//		ra.setFillAfter(true);
//	
//			// Start the animation
//		image.startAnimation(ra);
//		currentDegree = -playerAngle;
			
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}
}
