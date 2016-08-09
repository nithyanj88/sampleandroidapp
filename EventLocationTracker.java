package com.omt.quikformz.logic;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellLocation;

import com.omt.quikformz.model.LocationDataModel;
import com.omt.quikformz.util.CellLocationUtil;
import com.omt.quikformz.util.ExceptionHandler;

public class EventLocationTracker extends Handler implements Track{

	private Context context;

	private static LocationDataModel locationDataModel ;

	
	public EventLocationTracker(Context context) {
		this.context = context;
			System.out.println(" IN GET LOCATION DATA ************");
	    locationDataModel =  getLocationStuff();
	    System.out.println("locationDatamodel*************************************"+locationDataModel);
	    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));
	}
	
	@Override
	public void handleMessage(Message msg) {
		//locationDataModel =  getLocationStuff();
		
	}

	private void sleepFor(long milliSeconds) {
		this.removeMessages(0);
		sendMessageDelayed(obtainMessage(0), milliSeconds);
	}

	public LocationDataModel getLocationStuff() {
		System.out.println(" ************ get location stuff************");
		final LocationManager locationMnager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		/*if(!locationMnager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			enableGps();
		}*/
		Location loc = locationMnager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		System.out.println("location stuff ********************************************"+loc);
		
		if(loc != null){
			 locationDataModel = null;
			LocationDataModel tempLocationData = getLocationDataModel(loc);

			if(tempLocationData != null ) {
				locationDataModel = tempLocationData;
			}
			LocationDataForEvents.saveLocationDataforEvents(context,loc);
		//	Looper.myLooper().quit();
			//	sleepFor(trackingFrequency);	
		//	Looper.myLooper().quit();

		
			
		}else{


			LocationDataModel tempLocationData = getDummyLocationData();
			if(tempLocationData != null ) {
				locationDataModel = tempLocationData;
			}
			try{
				LocationDataForEvents.saveLocationDataforEventsForNull(context);
				System.out.println(" location data db ************** "+locationDataModel);
				//Looper.myLooper().quit();
			//	Looper.myLooper().quit();

			}catch (Exception e) {
				LocationDataForEvents.saveLocationDataforEventsForNull(context);
				System.out.println(" in lovcation exe ---------"+e.toString());
				//	Looper.myLooper().quit();


			}
		
		}
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				LocationDataModel tempLocationData = getDummyLocationData();
				if(tempLocationData != null ) {
					locationDataModel = tempLocationData;
				}
				try{
					LocationDataForEvents.saveLocationDataforEventsForNull(context);
					System.out.println(" location data db ************** "+locationDataModel);
					locationMnager.removeUpdates(this);
					Looper.myLooper().quit();

				}catch (Exception e) {
					LocationDataForEvents.saveLocationDataforEventsForNull(context);
					System.out.println(" in lovcation exe ---------"+e.toString());
					locationMnager.removeUpdates(this);
					Looper.myLooper().quit();
				}
			}

			@Override
			public void onLocationChanged(Location location) {
				System.out.println(" FROM L0CATION TRACKER ON LOCATION CHANGED========");

				locationDataModel = null;
				LocationDataModel tempLocationData = getLocationDataModel(location);

				if(tempLocationData != null ) {
					locationDataModel = tempLocationData;
				}
				LocationDataForEvents.saveLocationDataforEvents(context,location);
				locationMnager.removeUpdates(this);
				//	sleepFor(trackingFrequency);	
				Looper.myLooper().quit();

			}
		};
		locationMnager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
		if(locationDataModel == null){
			LocationDataModel tempLocationData = getDummyLocationData();
			if(tempLocationData != null ) {
				locationDataModel = tempLocationData;
			}
			LocationDataForEvents.saveLocationDataforEventsForNull(context);
			try{
			//	Looper.myLooper().quit();
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(" in lovcation exe ---------"+e.toString());
			}
		}
		
		System.out.println("LOCATION DATA MODEL ********  "+locationDataModel.toJson());
		return locationDataModel;
	}

	private LocationDataModel getLocationDataModel(Location loc){

		LocationDataModel locationDataModel = new LocationDataModel();
		double d = loc.getLatitude()*6000000;
		double d1 = loc.getLongitude()*6000000;
		int d2 = (int)d;
		int d3 = (int)d1;
		locationDataModel.lat = String.valueOf(d2);
		locationDataModel.lon = String.valueOf(d3);
		locationDataModel.acc = String.valueOf(loc.getAccuracy());
		locationDataModel.alt = String.valueOf(loc.getAltitude());        
		locationDataModel.speed = String.valueOf(loc.getSpeed());
		locationDataModel.direction = "0";
		locationDataModel.status = "OK";
		locationDataModel.type = 0;
		locationDataModel.landmark = "";
		locationDataModel.ts = Long.toString(System.currentTimeMillis());
		return locationDataModel;
	}

	private LocationDataModel getDummyLocationData() {
		LocationDataModel  locationDataForEvent = new LocationDataModel();
		CellLocationUtil cellLocation = new CellLocationUtil(context);
		locationDataForEvent.acc ="";
		locationDataForEvent.alt ="";
		locationDataForEvent.direction = "";
		locationDataForEvent.landmark = "";
		locationDataForEvent.lat = CellLocationUtil.strmyLatitude;
		locationDataForEvent.lon=CellLocationUtil.strmyLongitude;
		locationDataForEvent.speed ="";
		locationDataForEvent.status = "OK";
		locationDataForEvent.ts = Long.toString(System.currentTimeMillis());
		locationDataForEvent.type = 1;
		return locationDataForEvent;
	}

	@Override
	public LocationDataModel getViaPositionConnection() {
		// TODO Auto-generated method stub
		return locationDataModel;
	}
	/*private void enableGps(){
		String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	      
	    }*/
	//}

}
