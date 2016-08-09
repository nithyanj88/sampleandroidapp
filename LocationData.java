package com.omt.quikformz.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.activeandroid.ActiveRecordBase;
import com.activeandroid.annotation.Column;
import com.omt.quikformz.util.CellLocationUtil;
import com.omt.quikformz.util.ExceptionHandler;

public class LocationData extends ActiveRecordBase<LocationData>{


	public LocationData(Context context) {
		super(context);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));
		// TODO Auto-generated constructor stub
	}

	@Column(name ="lat")
	public String lat;

	@Column(name = "lon")
	public String lon;

	@Column(name = "direction")
	public String direction;

	@Column(name = "acc")
	public String acc;

	@Column(name = "landmark")
	public String landmark;

	@Column(name = "alt")
	public String alt;

	@Column(name = "type")
	public int type;

	@Column(name = "speed")
	public String speed;

	@Column(name = "status")
	public String status;

	@Column(name = "ts")
	public String ts;

	public LocationDataModel getLocationModel(){

		LocationDataModel dataModel = new LocationDataModel();
		dataModel.lat = lat;
		dataModel.lon = lon;
		dataModel.direction = direction;
		dataModel.acc = acc;
		dataModel.landmark = landmark;
		dataModel.alt = alt;
		dataModel.type = 0;
		dataModel.speed = speed;
		dataModel.status = "OK";

		return dataModel;

	}

	public static void saveLocationData(Context context, Location location){
		//clearLocationInfo(context);
		// EventInfo.querySingle(applicationContext, EventInfo.class, null, "eid = " + eventId, null);
		System.out.println(" in save location data ******************************************* DB");
		ArrayList<LocationData> list = null;
		try{
			list = LocationData.query(context.getApplicationContext(), LocationData.class, null );
		}catch (Exception e) {
			// TODO: handle exception
			try {
				list = LocationData.query(context.getApplicationContext(), LocationData.class, null );

			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		long timeStamp = 0l;
		long timeStamp2 = 0l;
		
		long Id = 0l;
		if(list != null){
			int listSize = list.size();
			if(list.size() >0){
				LocationDataModel LDM = new LocationDataModel();
				LDM.ts = list.get(listSize - 1).ts;
				timeStamp = Long.parseLong(LDM.ts);
				Id = list.get(listSize -1).getId();
				System.out.println(" Time Stamp =================== >>> "+LDM.ts);
			}
		}



		LocationData locationData = new LocationData(context.getApplicationContext());
		double d = location.getLatitude()*6000000;
		double d1 = location.getLongitude()*6000000;
		int d2 = (int)d;
		int d3 = (int)d1;
		locationData.lat = String.valueOf(d2);
		locationData.lon = String.valueOf(d3);
		locationData.acc = String.valueOf(location.getAccuracy());
		locationData.alt = String.valueOf(location.getAltitude());
		locationData.speed= String.valueOf(location.getSpeed()*3.6);
		locationData.direction = "0";
		locationData.type = 0;
		locationData.status = "OK";
		locationData.landmark = "";
		locationData.ts = Long.toString(System.currentTimeMillis());
		 timeStamp2 = Long.parseLong(locationData.ts);
		System.out.println(" time Stamp 2 ================>>> "+locationData.ts);

		try{
			if(timeStamp2- timeStamp <60000){
				LocationData locationDataold = LocationData.load(context.getApplicationContext(), LocationData.class, Id);
				locationDataold.delete(); 
			}

			locationData.save();

			System.out.println("CHECKING PHONE DATA ********* "+location.getLatitude());
		}catch (Exception e) {
			// TODO: handle exception
			if(timeStamp2- timeStamp < 60000){
				LocationData locationDataold = null;
				try {
					 locationDataold = LocationData.load(context.getApplicationContext(), LocationData.class, Id);
					 if(locationDataold != null)
							locationDataold.delete(); 
				} catch (Exception e2) {
					// TODO: handle exception
					try {
						 locationDataold = LocationData.load(context.getApplicationContext(), LocationData.class, Id);
						 if(locationDataold != null)
								locationDataold.delete(); 
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
				
				
			}
             try{
			locationData.save();
             }catch (Exception e1) {
				// TODO: handle exception
			}
		}

	}
	
	public static void saveLocationDataforEventsNoNetwork(Context context){
		System.out.println(" in save location cxdata for null ================== "+System.currentTimeMillis());

		ArrayList<LocationData> list = null;
		try{
			list = LocationData.query(context.getApplicationContext(), LocationData.class, null );
		}catch (Exception e) {
			// TODO: handle exception
			try {
				list = LocationData.query(context.getApplicationContext(), LocationData.class, null );

			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		long timeStamp = 0l;
		long timeStamp2 = 0l;
		long Id = 0l;
		if(list != null){
			int listSize = list.size();
			if(list.size() >0){
				LocationDataModel LDM = new LocationDataModel();
				LDM.ts = list.get(listSize - 1).ts;
				timeStamp = Long.parseLong(LDM.ts);
				Id = list.get(listSize -1).getId();
				System.out.println(" Time Stamp =================== >>> ");
			}
		}
		LocationData locationData = new LocationData(context.getApplicationContext());
		System.out.println(" in locationDataForEvent ============ ");
		CellLocationUtil cellLocation = new CellLocationUtil(context);
		locationData.acc ="";
		locationData.alt ="";
		locationData.direction = "";
		locationData.landmark = "";
		locationData.lat = "NA";
		locationData.lon="NA";
		locationData.speed ="";
		locationData.status = "OK";
		locationData.ts = Long.toString(System.currentTimeMillis());
		locationData.type = 1;
        timeStamp2 = Long.parseLong(locationData.ts);
		try{
			if(timeStamp2- timeStamp > 60000){
			locationData.save();	
			}List<LocationData> locationDataList = LocationData.query(context.getApplicationContext(), LocationData.class);
			System.out.println(" location data list *************** "+locationDataList.size());
		}catch (Exception e) {
			if(timeStamp2- timeStamp >60000){
			try{
				locationData.save();
			}
			catch (Exception e1) {
				// TODO: handle exception
			}	
			}List<LocationData> locationDataList = LocationData.query(context.getApplicationContext(), LocationData.class);
			System.out.println(" location data list execption*************** "+locationDataList.size());
		}
	}

	
	
	public static void saveLocationDataforEventsForNull(Context context){
		System.out.println(" in save location cxdata for null ================== "+System.currentTimeMillis());

		ArrayList<LocationData> list = null;
		try{
			list = LocationData.query(context.getApplicationContext(), LocationData.class, null );
		}catch (Exception e) {
			// TODO: handle exception
			try {
				list = LocationData.query(context.getApplicationContext(), LocationData.class, null );

			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		long timeStamp = 0l;
		long timeStamp2 = 0l;
		long Id = 0l;
		if(list != null){
			int listSize = list.size();
			if(list.size() >0){
				LocationDataModel LDM = new LocationDataModel();
				LDM.ts = list.get(listSize - 1).ts;
				timeStamp = Long.parseLong(LDM.ts);
				Id = list.get(listSize -1).getId();
				System.out.println(" Time Stamp =================== >>> ");
			}
		}
		LocationData locationData = new LocationData(context.getApplicationContext());
		System.out.println(" in locationDataForEvent ============ ");
		CellLocationUtil cellLocation = new CellLocationUtil(context);
		locationData.acc ="";
		locationData.alt ="";
		locationData.direction = "";
		locationData.landmark = "";
		locationData.lat = CellLocationUtil.strmyLatitude;
		locationData.lon=CellLocationUtil.strmyLongitude;
		locationData.speed ="";
		locationData.status = "OK";
		locationData.ts = Long.toString(System.currentTimeMillis());
		locationData.type = 1;               
        timeStamp2 = Long.parseLong(locationData.ts);
		try{
			if(timeStamp2- timeStamp > 60000){
			locationData.save();	
			}List<LocationData> locationDataList = LocationData.query(context.getApplicationContext(), LocationData.class);
			System.out.println(" location data list *************** "+locationDataList.size());
		}catch (Exception e) {
			if(timeStamp2- timeStamp >60000){
			try{
				locationData.save();
			}
			catch (Exception e1) {
				// TODO: handle exception
			}	
			}List<LocationData> locationDataList = LocationData.query(context.getApplicationContext(), LocationData.class);
			System.out.println(" location data list execption*************** "+locationDataList.size());
		}
	}


	public static void clearLocationInfo(Context context){
		System.out.println(" in clear info ***********");
		ArrayList<LocationData> list = null;
		try {
			list = LocationData.query(context.getApplicationContext(), LocationData.class,null);
		} catch (Exception e) {
			try {
				Thread.sleep(1000);
				list = LocationData.query(context.getApplicationContext(), LocationData.class,null);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		for(LocationData info: list){
			info.delete();
		}
		System.out.println(" after clear ***************");
	}
	public List<LocationDataModel> getLocationDataModel() {

		List<LocationDataModel> locationDataModels = new ArrayList<LocationDataModel>();
		ArrayList<LocationData> list = null ;
	   try{
		list = LocationData.query(getContext(), LocationData.class, null );
	   }catch (Exception e) {
		   try{
		 	list = LocationData.query(getContext(), LocationData.class, null );
		   }catch (Exception e1) {
		}
	}
	

		for(LocationData locationData: list){ 
			LocationDataModel locationDataModel = new LocationDataModel();
			locationDataModel.lat = locationData.lat;
			locationDataModel.lon = locationData.lon;
			locationDataModel.direction = locationData.direction;
			locationDataModel.acc = locationData.acc;
			locationDataModel.alt = locationData.alt;
			locationDataModel.type = locationData.type;
			locationDataModel.speed= locationData.speed;
			locationDataModel.status = "OK";
			locationDataModel.landmark = locationData.landmark;
			System.out.println(" value of ts ---->> : "+locationData);
			locationDataModel.ts = locationData.ts;


			locationDataModels.add(locationDataModel);
		}
		return locationDataModels;
	}


	public LocationDataModel getLoc(){

		LocationDataModel dataModel = new LocationDataModel();
		LocationData loc = LocationData.querySingle(getContext(), LocationData.class, null, "ts = " +ts , null);


		dataModel.lat = loc.lat;
		dataModel.lon = loc.lon;
		dataModel.direction = loc.direction;
		dataModel.acc = loc.acc;
		dataModel.alt = loc.alt;
		dataModel.type = loc.type;
		dataModel.speed= loc.speed;
		dataModel.status = "OK";
		dataModel.landmark = loc.landmark;
		dataModel.ts = loc.ts;


		return dataModel;
	}

	@Override
	public String toString() {
		return "LocationData [lat=" + lat + ", lon=" + lon + ", direction="
		+ direction + ", acc=" + acc + ", landmark=" + landmark
		+ ", alt=" + alt + ", type=" + type + ", speed=" + speed
		+ ", status=" + status + ", ts=" + ts + "]";
	}

}
