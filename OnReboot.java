package com.omt.quikformz.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Window;

import com.omt.quikformz.activity.ChangedDashboard;
import com.omt.quikformz.activity.DashBoard;
import com.omt.quikformz.activity.Login;
import com.omt.quikformz.model.SettingsInfo;
import com.omt.quikformz.util.SettingsDetails;

public class OnReboot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			gettingSetting(context);
			
		}

	}
	
private void gettingSetting(Context context) {
		
		SettingsInfo  settings = null;
		try{
			settings  = SettingsInfo.querySingle(context.getApplicationContext(), SettingsInfo.class, null, null);
		}catch (Exception e) {
			try {
				Thread.sleep(1000);
				settings  = SettingsInfo.querySingle(context.getApplicationContext(), SettingsInfo.class, null, null);

			} catch (InterruptedException e1) {
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

			Intent  mainmenu = (new Intent(context,ChangedDashboard.class));
			mainmenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainmenu.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			mainmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainmenu.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			context.startActivity(mainmenu);

		}
		else{
			Intent intent = new Intent(context, Login.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			context.startActivity(intent);
		}
	}

}
