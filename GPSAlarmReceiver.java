package com.omt.quikformz.logic;

import static com.omt.quikformz.util.IConstants.checkURL;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.omt.quikformz.model.LocationData;
import com.omt.quikformz.util.DateUtil;
import com.omt.quikformz.util.ExceptionHandler;
import com.omt.quikformz.util.SettingsDetails;
import com.omt.quikformz.util.upDateUtil;

public class GPSAlarmReceiver extends BroadcastReceiver implements LocationListener {

	Context gpscontext;
	private int startTime;
	private int endTime;
	private LocationManager locationManager;
	private String provider;
	public boolean gpsStatus = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out
		.println("-------------------GPS ALARAM RECIEVER----------------------------->");
		this.gpscontext = context;
		upDateUtil.gettingSetting(context);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));
		locationManager = (LocationManager) context
		.getSystemService(Context.LOCATION_SERVICE);
		provider = LocationManager.GPS_PROVIDER;
		startTime = Integer.parseInt(SettingsDetails.TRACKING_START_TIME);
		endTime = Integer.parseInt(SettingsDetails.TRACKING_END_TIME);

		if (SettingsDetails.AUTO_TRACKING_ENABLED.equals("1")) {
			System.out.println("==TRAKING IS ENABLED===========");
			if (DateUtil.trackingTimeValidity(startTime, endTime)) {
				if (!locationManager.isProviderEnabled(provider)) {
					enableGps();
				}
				ConnectivityManager connectivityManager = (ConnectivityManager) gpscontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

				boolean isEnabled = Settings.System.getInt(
					      context.getContentResolver(), 
					      Settings.System.AIRPLANE_MODE_ON, 0) == 1;
				if(!isEnabled){
				if (connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
						|| connectivityManager.getNetworkInfo(
								ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
					try {
						if(InetAddress.getByName(checkURL).isReachable(3000)){
							locationManager.requestLocationUpdates(provider, 10000, 0,
									GPSAlarmReceiver.this);
							Location location = locationManager.getLastKnownLocation(provider);
							System.out.println("location----gps alarm reciever------------->"+ location);
							if (location == null) {
								try {
									LocationData.saveLocationDataforEventsForNull(gpscontext);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								double lat = (location.getLatitude());
								double lng = (location.getLongitude());
								try {
									LocationData.saveLocationData(gpscontext, location);
									locationManager
									.removeUpdates(GPSAlarmReceiver.this);
								} catch (Exception e) {
									e.printStackTrace();

								}
							}
						} else {
							try {
								System.out.println("Network Is enable or not************************$$$$$$$$$$$$$$  saveLocationDataforEventsNoNetwork");
								LocationData.saveLocationDataforEventsNoNetwork(gpscontext);
								if (locationManager.isProviderEnabled(provider)) {
									disableGps();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						System.out
						.println("Network Is enable or not************************$$$$$$$$$$$$$$  saveLocationDataforEventsNoNetwork");
						LocationData
							.saveLocationDataforEventsNoNetwork(gpscontext);
						if (locationManager.isProviderEnabled(provider)) {
							disableGps();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else{
				try {
					System.out.println("Network Is enable or not************************$$$$$$$$$$$$$$  saveLocationDataforEventsNoNetwork");
					LocationData.saveLocationDataforEventsNoNetwork(gpscontext);
					if (locationManager.isProviderEnabled(provider)) {
						disableGps();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			} else {
				String provider = Settings.Secure.getString(
						gpscontext.getContentResolver(),
						Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				if (provider != null) {
					if (locationManager.isProviderEnabled(provider)) {
						disableGps();
					}
				}
			}
		}

	}

	private void enableGps() {
		String provider = Settings.Secure.getString(
				gpscontext.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
			"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			gpscontext.sendBroadcast(poke);
			gpsStatus = true;
		}

	}

	private void disableGps() {
		String provider = Settings.Secure.getString(
				gpscontext.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider.contains("gps")) {
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
			"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			gpscontext.sendBroadcast(poke);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}