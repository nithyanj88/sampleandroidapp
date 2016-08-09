package com.omt.quikformz.logic;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

import com.omt.quikformz.activity.Reassigned;
import com.omt.quikformz.model.AssignmentInfoModel;
import com.omt.quikformz.model.LocationDataModel;
import com.omt.quikformz.model.SettingsInfo;
import com.omt.quikformz.util.CellLocationUtil;
import com.omt.quikformz.util.DateUtil;
import com.omt.quikformz.util.IConstants;
import com.omt.quikformz.util.SettingsDetails;
import com.omt.quikformz.util.SyncUtil;

public class SyncWorker extends Service implements Runnable{

	private Context applicationContext;
	private Context activityContext;
	private Tracker gps = null;
	private static boolean LBSet=false;

	private Thread runner = null;
	private TrackerTask tracker = null;
	SettingsInfo settingsInfo ;
	public static boolean isTrackingEnabled;

	private static final int MAX_NUM_TRACKING_RECS = 15;
	long loginTime,nextAssignmentCheck,lastAssignmentCheck,assignmentfrequency;
	int assignmentFrequency;
	SyncUtil syncUtil;
	int count =0 ;
	
	public SyncWorker(Context context , Context applicationContext) {
		this.applicationContext = applicationContext ;
		this.activityContext = context;
		this.runner = new Thread(this);
		this.runner.start();
		 loginTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		try{
			System.out.println("*************in run method********************");
	
			assignmentFrequency = Integer.parseInt(SettingsDetails.ASSIGNMENT_CHECK_INTERVAL) ;
			System.out.println("*************in run method********************"+SettingsDetails.ASSIGNMENT_CHECK_INTERVAL);
			String isSchedule = SettingsDetails.ASSIGNMENT_ENABLED;
			assignmentfrequency = assignmentFrequency * 60000;
			 syncUtil = SyncUtil.instance(); 
			 lastAssignmentCheck = loginTime;
			

			while (true) {
				nextAssignmentCheck = lastAssignmentCheck +assignmentfrequency;
				long currentTime = System.currentTimeMillis();
				if(isSchedule != null && isSchedule.equals("1")){
				if(currentTime >=  nextAssignmentCheck){
					System.out.println("Start nextSendingTime ===========>>>> "+nextAssignmentCheck);
					try{
						//SyncUtil syncUtil = SyncUtil.instance();
						AssignmentInfoModel assignmentInfoModel = new AssignmentInfoModel(applicationContext);
						String resp = syncUtil.doPost(IConstants.URL, syncUtil.constructRequestMap(assignmentInfoModel.toJson()));
						System.out.println(" RESPONSE for UPDATED ASSIGNMENT ************* >>> :"+ (new JSONObject(resp).toString()));
						updateAssignments(resp);

					}catch (Exception e) {
						// TODO: handle exception
						System.out.println("@@@@@@@@@@@@@@@@@@@@in this loop");
					}
					lastAssignmentCheck = nextAssignmentCheck;
					//}
				}
				
				}else{
					break;
				}

			}

		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}


	private void sleepSecs(int secs) {
		System.out.println("### SyncTask:run() sleepSecs entered " + Thread.currentThread() );
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private boolean trackingTimeValidity(int startMin, int endMin) {
		boolean shouldTrack = false;
		System.out.println("validating startmin*************"+ startMin);
		System.out.println("endMin******************"+ endMin);
		System.out.println("start===end"+(startMin == endMin || startMin ==0 && endMin == 1439));

		if(startMin == endMin || startMin ==0 && endMin == 1439){
			shouldTrack = true;
		}
		if(startMin > endMin){
			Calendar cal = Calendar.getInstance();
			int currentTime = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
			if (!(endMin <= currentTime && startMin >= currentTime)) {
				shouldTrack = true;
			}
		}else{
			Calendar cal = Calendar.getInstance();
			int currentTime = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
			if (startMin <= currentTime && currentTime <= endMin) {
				shouldTrack = true;
			}
		}
		return shouldTrack;
	}




	private void sleepFor(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	class TrackerTask implements Runnable{

		private Thread runner = null;
		String trackingFrequency = null;
		boolean istracking = true;

		public TrackerTask() {

			this.runner = new Thread(this);
			this.runner.start();
		}

		public  boolean isAlive() {
			if(runner != null)
				return runner.isAlive();

			return false;
		}
		public void run(){

			System.out.println(" in run *going to TR ***********");			
			Looper.prepare();
			//while(istracking){
			//			gps = new LocationTracker(activityContext);	
			// Login login  = new Login();

			//		new Login().startService();
			//startService(new Intent(SyncWorker.this,LocTracker.class));

			//	LocationTracker locationTracker = new LocationTracker(activityContext, Long.parseLong(trackingFrequency));						
			System.out.println("*** SyncService:run() trackerThread: " + tracker + " created");
			istracking = false;
			Looper.loop();
			//break;
			//}
		}
	}

	private LocationDataModel getDummyLocationData() {
		LocationDataModel  locationDataForEvent = new LocationDataModel();
		CellLocationUtil cellLocation = new CellLocationUtil(activityContext);
		locationDataForEvent.acc ="";
		locationDataForEvent.alt ="";
		locationDataForEvent.direction = "";
		locationDataForEvent.landmark = "";
		locationDataForEvent.lat =CellLocationUtil.strmyLatitude;
		locationDataForEvent.lon=CellLocationUtil.strmyLongitude;
		locationDataForEvent.speed ="";
		locationDataForEvent.status = "OK";
		locationDataForEvent.ts = Long.toString(System.currentTimeMillis());
		locationDataForEvent.type = 1;

		System.out.println(" DAte ============= >> "+DateUtil.convertLongToDate(Long.parseLong(locationDataForEvent.ts)));
		return locationDataForEvent;
	}

	public LocationDataModel createLocationData(LocationDataForEvents location){

		System.out.println(" in LOCATION DATA MODEL ********************" +location);
		LocationDataModel locationDataModel = new LocationDataModel();

		if(location != null){


			locationDataModel.direction = location.direction;
			locationDataModel.lat = location.lat;
			locationDataModel.acc = location.acc;
			locationDataModel.landmark = location.landmark;
			locationDataModel.alt = location.alt;
			locationDataModel.lon = location.lon;
			locationDataModel.type = location.type;
			locationDataModel.speed = location.speed;
			locationDataModel.status = location.status;
			locationDataModel.ts = location.ts;
		} else{

			locationDataModel.acc ="";
			locationDataModel.alt ="";
			locationDataModel.direction = "";
			locationDataModel.landmark = "";
			locationDataModel.lat = CellLocationUtil.strmyLatitude;
			locationDataModel.lon=CellLocationUtil.strmyLongitude;
			locationDataModel.speed ="";
			locationDataModel.status = "OK";
			locationDataModel.ts = Long.toString(System.currentTimeMillis());
			locationDataModel.type = 1;

		}

		return locationDataModel;
	}


	public void updateAssignments(String response){

		try {
			JSONObject jObj = new JSONObject(response);
			JSONArray newAssignmentArray = jObj.getJSONArray("workflows");
			ArrayList<AssignmentInfo> previousAssignmentIdList = AssignmentInfo.query(applicationContext, AssignmentInfo.class);

			if(newAssignmentArray != null && newAssignmentArray.length() >0){
				int newAssignmentLength = newAssignmentArray.length();
				for(int i =0 ; i < newAssignmentLength ; i++){
					if(previousAssignmentIdList != null && previousAssignmentIdList.size() > 0){
						for(int j = 0 ; j<previousAssignmentIdList.size() ; j++){	
							String oldAssignmentId = previousAssignmentIdList.get(i).asId;
							String newAssignmentId = newAssignmentArray.getJSONObject(i).getString("asId");
							String oldWorkflowId = previousAssignmentIdList.get(i).id;
							String newWorkflowId = newAssignmentArray.getJSONObject(i).getString("id");
							int oldAssignmentStatus = previousAssignmentIdList.get(i).assignStatus;
							int oldAStatus = previousAssignmentIdList.get(i).status;
							int newStatus = Integer.parseInt(newAssignmentArray.getJSONObject(i).getString("status"));
							System.out.println(" OLD WORKFLOW ID --- >> : "+oldAssignmentId + "  NEW WORKFLOW ID ---- >>> : "+newAssignmentId);
							System.out.println(" OLD ASSIGNMENT STATUS  --- >> : "+oldAStatus + "  NEW ASSIGNMENT STATUS ---- >>> : "+newStatus);
							if(oldAssignmentId .equals(newAssignmentId)){
								if(oldWorkflowId .equals(newWorkflowId)){
									AssignmentInfo information = AssignmentInfo.querySingle(applicationContext, AssignmentInfo.class ,null,"asId = "+oldAssignmentId);
									if(oldAStatus != newStatus && newStatus == 2){
										System.out.println(" IN STATUS NOT EQUAL *******************");
										information.status = newStatus;
									}
									information.id = newWorkflowId;
									information.save();

								}else{
									if(oldAssignmentStatus != 1){
										AssignmentInfo information = AssignmentInfo.querySingle(applicationContext, AssignmentInfo.class ,null,"asId = "+oldAssignmentId);
										information.id = newWorkflowId;
										information.save();
									}else{
										AssignmentInfo information = AssignmentInfo.querySingle(applicationContext, AssignmentInfo.class ,null,"asId = "+oldAssignmentId);
										information.id = newWorkflowId;
										information.save();
									}
								}
							}else{
								System.out.println(" SAVE INFO ----------------- >>> : "+newAssignmentArray.get(i));
								System.out.println(" SAVE INFO >> : "+(JSONObject)newAssignmentArray.get(i));
								saveAssignmentData((JSONObject) newAssignmentArray.get(i));
							}

						}

					}else{
						saveAssignmentData((JSONObject) newAssignmentArray.get(i));
					}


				}		
			}
			checkAssignmentTableSize();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void saveAssignmentData(JSONObject obj) {
		// TODO Auto-generated method stub

		System.out.println(" IN SAVE ASSIGNMENT ----------------- >>> : "+obj.toString());

		try{
			System.out.println(" INFO --------------- >>> "+obj.getString("id"));
			AssignmentInfo assignmentInfo = new AssignmentInfo(applicationContext);
			assignmentInfo.asId = obj.getString("asId");
			assignmentInfo.id = obj.getString("id");
			assignmentInfo.assgnName = obj.getString("assgnName");
			assignmentInfo.name = obj.getString("name");
			assignmentInfo.scheduleEndTime = obj.getLong("scheduleEndTime");
			assignmentInfo.scheduleStartTime = obj.getLong("scheduleStartTime");
			assignmentInfo.text = obj.getString("text");
			assignmentInfo.status = obj.getInt("status");

			assignmentInfo.save();

		}catch (Exception e) {
			e.printStackTrace();

		}
	}
	private void checkAssignmentTableSize() {

		ArrayList<AssignmentInfo> previousAssignmentIdList = AssignmentInfo.query(applicationContext, AssignmentInfo.class);
		System.out.println(" SIZE OF ASSIGNMENT TABLE *********************** : "+previousAssignmentIdList.size());

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}