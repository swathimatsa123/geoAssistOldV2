package com.geoassist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.geoassist.gpsDlg.gpsDlgListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import android.location.LocationListener;

public class collectTab extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
                                                    GooglePlayServicesClient.OnConnectionFailedListener,
                                                    LocationListener,
                                                    OnItemSelectedListener,
                                                    OnMarkerDragListener,
                                                    OnNavigationListener,
                                                    gpsDlgListener{

	private SupportMapFragment fragment;
	private GoogleMap map;
    private double currentLat = 0;
    private double currentLng = 0;
    private double prevLat = 0;
    private double prevLng = 0;
    private LatLng myPos =new LatLng(currentLat,currentLng);
    private Marker myPosMarker;
    LocationRequest mLocationRequest;
    private LocationClient locationclient;
    private Context  myCxt;
    private Polyline myPath = null;
    
    private static final int CAMERA_ACTIVITY = 100;
    private static final int COMPASS_ACTIVITY = 200;
    
    private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL =MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    private View rootView = null;
    private	Boolean ignoreGps = false; 
    private File mediaFile=null;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.collect_tab,container, false);
		return rootView;
	}
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	String[] actions = new String[] {
    			"Road",
    		    "Sattelite",
    		    "Hybrid",
    		    "Terrain"};
    	super.onCreate(savedInstanceState);
    	setHasOptionsMenu(true);
//    	getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//    			android.R.layout.simple_spinner_dropdown_item, 
//    			actions);
//
//        /** Setting dropdown items and item navigation listener for the actionbar */
//        getActivity().getActionBar().setListNavigationCallbacks(adapter, this);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		android.support.v4.app.FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
		if (fragment == null) {
		    fragment = SupportMapFragment.newInstance();
		    fm.beginTransaction().replace(R.id.mapView, fragment).commit();
		}
		map = fragment.getMap();
		if (map!= null){
			map.setOnMarkerDragListener(this);
		}
		myCxt = getActivity();

		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(myCxt,this, this);
			locationclient.connect();
		}
		else{
			Toast.makeText(myCxt, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}				
		 mLocationRequest = LocationRequest.create();
	     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     mLocationRequest.setInterval(UPDATE_INTERVAL);
	     mLocationRequest.setFastestInterval(FASTEST_INTERVAL);	
	}

	@Override
	public void onResume() {
		super.onResume();
	}
 

	@Override
	public void onLocationChanged(Location location) {
		prevLat = currentLat;
		prevLng = currentLng;
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();
		myPos = new LatLng(currentLat,currentLng);
		if (map == null)
		{
			return;
		}
		GeomagneticField geoField = new GeomagneticField(
		         Double.valueOf(location.getLatitude()).floatValue(),
		         Double.valueOf(location.getLongitude()).floatValue(),
		         Double.valueOf(location.getAltitude()).floatValue(),
		         System.currentTimeMillis()
		      );
//		location.bearingTo(dest)
		if (ignoreGps != true){
		    if (map == null) {
			    map = fragment.getMap();
			    if (map != null) 
			    {
			    	map.setOnMarkerDragListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions().position(myPos).title(String.valueOf(currentLat)+"\n" +String.valueOf(currentLng)));
			    	myPosMarker.setDraggable(true);
			    	myPosMarker.showInfoWindow();
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
				}
			}
			else{
				myPosMarker.setPosition(myPos);
			}
			if (((prevLat != 0) && 
				 (prevLng != 0) && 
				 (currentLat != 0) &&
				 (currentLng != 0)) &&
				 (prevLat != currentLat) && 
				 (prevLng != currentLng) )
			{	
				myPath = map.addPolyline(new PolylineOptions()
									.add(new LatLng(prevLat, prevLng), new LatLng(currentLat, currentLng))
									.width(5)
									.color(Color.BLUE));
				myPath.setVisible(true);
			}
	    }
	}


	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
//        Toast.makeText(myCxt, "Connected", Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        locationclient.requestLocationUpdates(mLocationRequest, (com.google.android.gms.location.LocationListener) this);

	}


	@Override
	public void onDisconnected() {
		
		// My test push
		// TODO Auto-generated method stub
		
	}

	public void invokeCamera()
	{
    	Intent mIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	mIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri()); // set the image file name
    	startActivityForResult(mIntent, 100);
	}

	public void invokeCompass()
	{
    	Intent mIntent= new Intent(getActivity().getApplicationContext(),CompassActivity.class);
    	startActivityForResult(mIntent, COMPASS_ACTIVITY);
	}
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		   Toast.makeText(myCxt, "Activity completed", Toast.LENGTH_LONG).show();
	        if (resultCode == Activity.RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	galleryAddPic();
	        	if (data != null){ 
	            	Toast.makeText(myCxt, "Image saved to:" +
	                     data.getData()+"---", Toast.LENGTH_LONG).show();
	            }
	        } else if (resultCode == Activity.RESULT_CANCELED) {
//	        	Toast.makeText(myCxt, "Cancelled Activity:" , Toast.LENGTH_LONG).show();
	        } else {
//	        	Toast.makeText(myCxt, "Failed Activity:" , Toast.LENGTH_LONG).show();
	        }
	}

	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(mediaFile);
	    mediaScanIntent.setData(contentUri);
	    getActivity().sendBroadcast(mediaScanIntent);
	   
	    Log.e(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString(),"URI1");
	    Log.e(contentUri.toString(),"URI2");
	    Intent editIntent = new Intent(Intent.ACTION_EDIT);
	    editIntent.setDataAndType(contentUri, "image/*");
	    editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    startActivity(Intent.createChooser(editIntent, null));
	}
	
	private Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(){

		   File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "GeoAssist");
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("GeoAssist", "failed to create directory");
		            return null;
		        }
		    }

		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    mediaFile = new File(mediaStorageDir.getPath() + File.separator +"geoAssist_"+ timeStamp + ".jpg");
//		    Log.e("FileName" , "Return File:" + mediaStorageDir.getPath()+ File.separator  + "geoAssist_" + timeStamp + ".jpg");
	    return mediaFile;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		int mapType = 0;
		if (map != null) {
			switch ((int)itemPosition){
			case 0:
				mapType = GoogleMap.MAP_TYPE_NORMAL;
				break;
			case 1:
				mapType = GoogleMap.MAP_TYPE_SATELLITE;
				break;			
			case 2:
				mapType = GoogleMap.MAP_TYPE_HYBRID;
				break;			
			case 3:
				mapType = GoogleMap.MAP_TYPE_TERRAIN;
				break;			
			}
			map.setMapType(mapType);
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		int mapType = 0;
		if (map != null) {
			switch ((int)id){
			case 0:
				mapType = GoogleMap.MAP_TYPE_NORMAL;
				break;
			case 1:
				mapType = GoogleMap.MAP_TYPE_SATELLITE;
				break;			
			case 2:
				mapType = GoogleMap.MAP_TYPE_HYBRID;
				break;			
			case 3:
				mapType = GoogleMap.MAP_TYPE_TERRAIN;
				break;			
			}
			map.setMapType(mapType);
		}

	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    private void showGpsDialog() {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        gpsDlg gpsDialog = new gpsDlg();
        gpsDialog.frg = this;
        gpsDialog.show(fm, "fragment_edit_name");
    }

    @Override
    public void onGpsGetDialog(String lat, String lng) {
    	ignoreGps =true;
    	myPos =new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
    	myPosMarker.remove();
    	if (map == null) {
		    map = fragment.getMap();
    	}
	    myPosMarker = map.addMarker(new MarkerOptions().position(myPos).title(String.valueOf(lat)+ "\n"+ String.valueOf(lng)));
	    myPosMarker.setDraggable(true);
	    myPosMarker.showInfoWindow();
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
    }

	@Override
	public void onMarkerDrag(Marker marker) {
		LatLng pos = marker.getPosition();
		Log.e("DragPos:", String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.setTitle(String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.showInfoWindow();
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		LatLng pos = marker.getPosition();
		Log.e("DragEnd:", String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.setTitle(String.valueOf(pos.latitude)+"\n"+String.valueOf(pos.longitude));
		myPosMarker.showInfoWindow();
    	ignoreGps =true;
    	myPos =new LatLng(pos.latitude,pos.longitude);
	}


	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.map_view_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ic_camera:
        	MainActivity  ma = (MainActivity)getActivity();
        	if (ma != null){
        		invokeCamera();
        	}
            break;

		case R.id.ic_gps_fix:
        	showGpsDialog();
        	break;
		case R.id.ic_compass:
        	invokeCompass();
        	break;        	
		}
		return true;
	}
}
