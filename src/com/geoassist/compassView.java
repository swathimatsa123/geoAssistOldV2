package com.geoassist;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class compassView extends View  {
//public class compassView extends View  implements SensorEventListener {
	float azimut = 0;
	float angle  = 0;
	int   updRcvd = 0;
	float x=0, y=0, z = 0;
	
	float [] rotation;
//	float [] orientation;
	Paint paint = new Paint();
    SensorManager sensorManager = null;
    String orStr;
    String rotStr;
    String rotStr1;
    String rotStr2;
    String rotStr3;
    float  posX = 0;
    float  posY = 0;
    float  posZ = 0;
    public compassView(Context context) {
    	super(context);
    	init();
    };
    public void init () {
		paint.setColor(0xff00ff00);
		paint.setStyle(Style.STROKE);
		paint.setTextSize(80);
		paint.setStrokeWidth(4);
		paint.setAntiAlias(true);
		sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
//    	sensorManager.registerListener(this,
//						sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//						SensorManager.SENSOR_DELAY_NORMAL);
//		
//    	sensorManager.registerListener(this, 
//    			sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
//				SensorManager.SENSOR_DELAY_UI);
//		
//    	sensorManager.registerListener(this, 
//    			sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
//	            SensorManager.SENSOR_DELAY_UI);		
//		    	
    	
    }

	public compassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public compassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
    protected void onDraw(Canvas canvas) {
    	int width = getWidth();
    	int height = getHeight();
    	int centerx = width/2;
	    int centery = height/2;
	    int origin_X = height -100;
	    int origin_Y = 100;
	    int pX = 10;
	    int pY = 10;
	    int space = 50;
	    int offset = 100;
	    int y = offset;
	    int x = offset;
	    int xx = width -offset;
	    int yy = offset;
	    
	    int vx = offset;
	    int vy = offset;
	    int vxx = vx;
	    int vyy = height - offset; 
	    
	    int mx = (width - offset)/2;
	    int my =  offset;
	    int mxx = (width - offset)/2;
	    int myy =  height - offset;
	    int theta = (int)this.angle;
	    if (updRcvd != 0){
//	    	float x2 = 0;
//	    	float y2 = 0;
//	    	float radians = (float) Math.toRadians(-azimut*360/(2*3.14159f));
//	    	x2 = height/2 * (float) Math.cos(radians);
//	    	y2 = height/2 * (float) Math.sin(radians);
//	    	paint.setStrokeWidth(8);
//	    	paint.setColor(Color.GREEN);
//
//	    	canvas.drawLine(centerx - (float)(x2), centery -(float)(y2), centerx + (float)x2, centery + (float)y2 ,paint);
//	    	paint.setStrokeWidth(4);
//		    canvas.drawText("E", centerx + (float)x2 -space, centery + (float)y2 -space, paint);
//		    canvas.drawText("W", centerx - (float)x2 -space, centery - (float)y2 -space, paint);
	    }

	    paint.setColor(0xff0000ff);
//	    Log.e("H: " + String.valueOf(height)+ " W: "+ String.valueOf(width), 
//	    	  " X: " +String.valueOf(x)+ 
//	    	  " Y: " + String.valueOf(y)+
//	    	  " XX: " +String.valueOf(xx)+ 
//	    	  " YY: " +String.valueOf(yy));
	    canvas.drawLine(x, y, xx, yy, paint);
//	    canvas.drawLine(vx, vy, vxx, vyy, paint);
//	    canvas.drawLine(mx, my, mxx, myy, paint);
	    int px = mx;
	    int py = my;
	    int pxx = width - offset;
	    int pyy = (int) (Math.tan(theta) *(pxx -mx));	   
	    canvas.drawLine(px, py, pxx, pyy, paint);

//	    canvas.drawText("Angle : " + String.valueOf(this.angle), 20,300+centery, paint);  
	    float degrees = (float) Math.toDegrees(Math.toRadians(-azimut*360/(2*3.14159f)));
//	    Log.e("AZIMUT" , "Caclulated " + String.valueOf(degrees) + "Might be: " +String.valueOf(Math.toDegrees(-azimut)));
	    canvas.drawText("Azimuth : " + String.valueOf(degrees), 20,200, paint); 
	    canvas.drawText(" X: " + String.valueOf(this.x), 20,300, paint); 
	    canvas.drawText(" Y: " + String.valueOf(this.y),20,400, paint); 	
	    canvas.drawText(" Z: " + String.valueOf(this.z), 20,500, paint); 

	    //	    String rotStr = "Rot: ";
//	    for (int i=0; i < this.rotation.length; i++) {
//	    	rotStr = rotStr + " : " +String.valueOf(this.rotation[i]);
//	    }
//	    if (this.rotStr != null){
//	    	Log.e("RotStr", this.rotStr);
//	    	canvas.drawText(this.rotStr, 20,500+centery, paint); 
//	    	canvas.drawText(this.rotStr1, 20,600+centery, paint); 
//	    	canvas.drawText(this.rotStr2, 20,700+centery, paint); 
//	    	canvas.drawText(this.rotStr3, 20,800+centery, paint); 
//	    }
//	    String orStr = "Ori: ";
//	    for (int i=0; i < this.orientation.length; i++) {
//	    	orStr  = orStr  + " : " +String.valueOf(this.orientation[i]);
//	    }
//	    if (this.orStr != null){
//	    	Log.e("orStr", this.orStr);
//		    canvas.drawText(orStr, 20,900+centery, paint); 
//	    }
	    //	    paint.setColor(0xff00ff00);
    }
    
    public void update (float azimut, float angle, float[] r, float x, float y, float z) {
    	this.azimut  = azimut;
    	this.angle = angle;
    	this.x = x;
    	this.y = y;
    	this.z = z;
//    	this.rotation = new float[r.length];
//    	this.orientation = new float[orientation.length];
//    	this.rotation = r;
//    	this.orientation = orientation;
    	this.rotStr = "Rotation Vector: ";
    	this.rotStr1 = "";
    	for (int i=0; i < 3; i++) {
	    	this.rotStr1 = this.rotStr1 + " : " +String.valueOf(r[i]);
	    }
    	
    	this.rotStr2 = "";
    	for (int i=3; i < 6; i++) {
	    	this.rotStr2 = this.rotStr2 + " : " +String.valueOf(r[i]);
	    }

    	this.rotStr3 = "";
    	for (int i=6; i < 9; i++) {
	    	this.rotStr3 = this.rotStr3 + " : " +String.valueOf(r[i]);
	    }	
    	this.orStr = "Ori : ";
//    	for (int i=0; i < orientation.length; i++) {
//	    	this.orStr = this.rotStr + " : " +String.valueOf(orientation[i]);
//	    }
//    	Log.e("ORIENT", this.orStr);
    	updRcvd = 1;
    	invalidate();
    
//    	Log.e("Azimut :" ,String.valueOf(-azimut*360/(2*3.14159f)));
    }
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//	    	float[] values = event.values;
//	        float x = values[0];
//	        float y = values[1];
//	        float z = values[2];
//	        if (((int)x != (int)posX) || ((int)y != (int)posY) ||((int)z != (int)posZ))
//	        {
//	        	posX = x;
//	        	posY = y;
//	        	posZ = z;
////	        	Log.e("From Sensor", " X: "+String.valueOf(x)+ " Y: "+String.valueOf(y)+ " Z: "+String.valueOf(z) );
//	        }
//	    }
//	}
  }