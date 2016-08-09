package com.omt.quikformz.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.omt.quikformz.activity.NetworkAlert;

public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try{

			ConnectivityManager connManager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);



			TelephonyManager telephonyManager = (TelephonyManager) context
			.getSystemService(Context.TELEPHONY_SERVICE);
			System.out.println("NETWORK CHANGED..............");
			if(!(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) && !(mWifi.isConnected())){
				System.out.println("NETWORK IS Disabled..............");
	       /* Intent showalert = new Intent(context,NetworkAlert.class);
				showalert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(showalert);*/
	    	Toast.makeText(context, "Network is not available",Toast.LENGTH_LONG).show();
	        
			}
		}
		catch(Exception ex){
			System.out.println("Error"+ ex.toString());
		}
	}
}
