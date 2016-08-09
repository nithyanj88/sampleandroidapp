package com.omt.quikformz.activity;

import static com.omt.quikformz.util.IConstants.A_ID;
import static com.omt.quikformz.util.IConstants.A_STATUS;
import static com.omt.quikformz.util.IConstants.ENFORCE_ORDER;
import static com.omt.quikformz.util.IConstants.EVENT_ID_LIST;
import static com.omt.quikformz.util.IConstants.E_IDs;
import static com.omt.quikformz.util.IConstants.MANDATORY_EVENT_ID_LIST;
import static com.omt.quikformz.util.IConstants.NON_MANDATORY_EVENT_ID_LIST;
import static com.omt.quikformz.util.IConstants.REPEATABLE_EVENT_ID_LIST;
import static com.omt.quikformz.util.IConstants.W_ID;
import static com.omt.quikformz.util.IConstants.W_NAME;

import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TabHost;

import com.omt.quikformz.R;
import com.omt.quikformz.model.EventInfo;
import com.omt.quikformz.model.SettingsInfo;
import com.omt.quikformz.util.IConstants;
import com.omt.quikformz.util.SettingsDetails;
import com.omt.quikformz.util.upDateUtil;

public class EventsTab extends TabActivity{
	Context activitycontext;
	ImageView scheduleIcon;
	TabHost mTabHost;
	Context context;
	String getWorkflowId = "";
	String getAssignmentId = "";
	String getWorkflowName = "";
	String getRouteName = "";
	String getCustomerName = "";
	String[] getEventIds;	
	int getAssignmentStatus;
	String getEnforceOrder;
	String getclassName ;
	String[] getEventIdList;
	String[] getMandatoryEventIdList;
	String[] getNonMandatoryEventIdList;
	String[] getRepeatableEventIdList;
	String[] eventids;
	EventInfo eventInfo,eventinfo1;
	public static List<EventInfo> resultList;
	private List<String> mandatoryEventIdList;
	private List<String> nonMandatoryEventIdList;
	private List<String> repeatableEventIdList;
	private List<String> eventIdList;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        context = this;
	      //  activitycontext = this;
	       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.dashboard);
	        upDateUtil.gettingSetting(context.getApplicationContext());
	       
	       // gettingSetting();
			Bundle extras = getIntent().getExtras();
			if(extras != null){
				setTitle(extras.getString(W_NAME));
				getWorkflowId = extras != null ? extras.getString(W_ID) : null;
				getRouteName = extras != null ? extras.getString(IConstants.ROUTE_NAME) : null;
				getCustomerName = extras != null ? extras.getString(IConstants.CUSTOMER_NAME) : null;
				getAssignmentId = extras != null ? extras.getString(A_ID) : null;
				getWorkflowName = extras != null ? extras.getString(W_NAME) : null;
				getEventIds = extras != null ? extras.getStringArray(E_IDs) : null;
				getAssignmentStatus = extras != null ? extras.getInt(A_STATUS) : 0;
				getEnforceOrder = extras != null ? extras.getString(ENFORCE_ORDER) : null;
				getclassName = extras != null ? extras.getString("class name") : null;
				getEventIdList = extras != null ? extras.getStringArray(EVENT_ID_LIST) : null;
				getMandatoryEventIdList = extras != null ? extras.getStringArray(MANDATORY_EVENT_ID_LIST) : null;
				getNonMandatoryEventIdList = extras != null ? extras.getStringArray(NON_MANDATORY_EVENT_ID_LIST) : null;
				getRepeatableEventIdList = extras != null ? extras.getStringArray(REPEATABLE_EVENT_ID_LIST) : null;
				
			}
	        TabHost tabHost = getTabHost();
	        
	       /* getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);
	        scheduleIcon  = (ImageView) findViewById(R.id.quickformzIcon);
			scheduleIcon.setImageResource(R.drawable.title_logo);*/
			Intent intent = new Intent(context, EventsWithIcon.class);
			intent.putExtra(W_NAME, getWorkflowName);
			intent.putExtra(W_ID, getWorkflowId);
			intent.putExtra(ENFORCE_ORDER, getEnforceOrder);
			intent.putExtra("class name", getclassName);
			intent.putExtra(E_IDs, getEventIds);
			intent.putExtra(IConstants.ROUTE_NAME, getRouteName);
			intent.putExtra(IConstants.CUSTOMER_NAME, getCustomerName);

			System.out.println(" get ROUTE NAME ----------------------------- "+getRouteName);
			intent.putExtra(EVENT_ID_LIST, getEventIdList);
			
			
			
			   tabHost.addTab(tabHost.newTabSpec(getWorkflowName).setIndicator(getWorkflowName,context.getResources().getDrawable(R.drawable.tabiconevents)).setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
			   			   
//			   if(SettingsDetails.ASSIGNMENT_ENABLED.equals("1")){
//		        	 tabHost.addTab(tabHost.newTabSpec("Reminder").setIndicator("Reminder",context.getResources().getDrawable(R.drawable.tableiconforassignment)).setContent(new Intent(context, Assignment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
//		        }		      
//			   tabHost.addTab(tabHost.newTabSpec("My Reports").setIndicator("My Reports",context.getResources().getDrawable(R.drawable.tabiconformyreports)).setContent(new Intent(context, MyReport.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
//			 
//				if (SettingsDetails.ENABLE_ROUTE.equals("1")) {
//			   tabHost.addTab(tabHost.newTabSpec("Route").setIndicator("Route",EventsTab.this.getResources().getDrawable(R.drawable.tabiconsforroute)).setContent(new Intent(EventsTab.this, Route.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
//				}
			   tabHost.setCurrentTab(0);		       
	       // new DashBoardPresenter(activitycontext);
	    }
	 
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				return true;
			}

			return super.onKeyDown(keyCode, event);
		}
	 
	 
		private void gettingSetting() {
			SettingsInfo  settings = null;
			try{
		      settings  = SettingsInfo.querySingle(getApplicationContext(), SettingsInfo.class, null, null);
			}catch (Exception e) {
				try {
					Thread.sleep(3000);
				      settings  = SettingsInfo.querySingle(getApplicationContext(), SettingsInfo.class, null, null);

				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			if(settings != null){
			SettingsDetails.AUTO_TRACKING_ENABLED = settings.AUTO_TRACKING_ENABLED;
			SettingsDetails.ASSIGNMENT_ENABLED = settings.ASSIGNMENT_ENABLED;
			SettingsDetails. TRACKING_FREQUENCY = settings.TRACKING_FREQUENCY;
			SettingsDetails. SEND_LOW_BATTERY_ALERT = settings.SEND_LOW_BATTERY_ALERT;
			SettingsDetails. ENABLE_AUDIO = settings.ENABLE_AUDIO;
			SettingsDetails.SENDING_FREQUENCY = settings.SENDING_FREQUENCY;
			SettingsDetails.ENABLE_PICTURE = settings.ENABLE_PICTURE;
			SettingsDetails.ASSIGNMENT_CHECK_INTERVAL =settings.ASSIGNMENT_CHECK_INTERVAL;
			SettingsDetails.SAMPLING_FREQUENCY = settings.SAMPLING_FREQUENCY;
			SettingsDetails.TRACKING_END_TIME = settings.TRACKING_END_TIME;
			SettingsDetails.TRACKING_START_TIME = settings.TRACKING_START_TIME;	
			SettingsDetails.SMS_ENABLED = settings.SMS_ENABLED;
			SettingsDetails.LOG_OUT = settings.LOG_OUT;
			SettingsDetails.CONNECT_BLUETOOTH = settings.CONNECT_BLUETOOTH;
			if(settings.ENABLE_MESSAGING != null){
		    	SettingsDetails.ENABLE_MESSAGING = settings.ENABLE_MESSAGING;
		    	}else{
		    		SettingsDetails.ENABLE_MESSAGING = "0";
		    	}
			}
			
			if (settings.CHECK_FOR_UPDATE != null) {
				SettingsDetails.CHECK_FOR_UPDATE = settings.CHECK_FOR_UPDATE;
			} else {
				SettingsDetails.CHECK_FOR_UPDATE = "1";
			}
			if (settings.ENABLE_SUBCUSTOMER != null) {
				SettingsDetails.ENABLE_SUBCUSTOMER = settings.ENABLE_SUBCUSTOMER;
			} else {
				SettingsDetails.ENABLE_SUBCUSTOMER = "0";
			}
			
			if(settings.ENABLE_ROUTE != null){
	        	SettingsDetails.ENABLE_ROUTE = settings.ENABLE_ROUTE;
	        	}else{
	        		SettingsDetails.ENABLE_ROUTE = "1";
	        	}
	    	
		}
}
