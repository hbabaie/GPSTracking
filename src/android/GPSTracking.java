package com.pakhshyaran.kara.plugins;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import android.location.*;
import android.app.Activity;
import android.content.Intent;

public class GPSTracking extends CordovaPlugin {
	public static final String ACTION_START_LISTENING = "StartListening";
	String RemoteServerAddress;
	String PersonnelId;
	int Interval;
	LocationManager lm;
	boolean gps_enabled=false;
    boolean network_enabled=false;
    Criteria criteria;
	LocationListener listener;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			if (ACTION_START_LISTENING.equals(action)) { 
				JSONObject arg_object = args.getJSONObject(0);
				RemoteServerAddress = arg_object.getString("RemoteServer");
				PersonnelId = arg_object.getString("PersonnelId");
				Interval = arg_object.getInt("Interval");
				criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
				criteria.setCostAllowed(true);
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setSpeedRequired(false);
				
//				listener = new LocationListener() {
//			        public void onLocationChanged(Location location) {
//		        		//SendDataToServer(location);
//			        }
//			        public void onProviderDisabled(String provider) {}
//			        public void onProviderEnabled(String provider) {}
//			        public void onStatusChanged(String provider, int status, Bundle extras) {}
//			    };
//				
//				lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//				try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
//		        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
//		        if(!gps_enabled && !network_enabled)
//		        	callbackContext.error("There's no active location provider on your device.");
//				 
//		        lm.requestLocationUpdates(Interval, 0, criteria, listener);
//		        
//				this.cordova.getThreadPool().execute(new Runnable() {
//				    public void run() {
//				        // Main Code goes here
//				        callbackContext.success();
//				    }
//				});
		        
				callbackContext.success();
				return true;
			}
			callbackContext.error("InvalidCommand!");
			return false;
		} catch(Exception e) {
			System.err.println("Error: " + e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
	}
	
	public void SendDataToServer(Location location)
	{
		String URL = "http://" + RemoteServerAddress + "//OuterInterface/Mobile/SetGPSTrackingRecord?" +
				"PersonnelId=" + PersonnelId +
				"&Latitude=" + location.getLatitude() +
				"&Longitude=" + location.getLongitude() +
				"&LocationProvider=" + location.getProvider() +
				"&Accuracy=" + (location.hasAccuracy() ? location.getAccuracy() : Float.MAX_VALUE) +
				"&Time=" + location.getTime();
		try
		{
			HttpClient Client = new DefaultHttpClient();
			String SetServerString = "";
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			SetServerString = Client.execute(httpget, responseHandler);
			//content.setText(SetServerString);
		}
		catch(Exception ex)
		{
			//content.setText("Fail!");
		}
	}
}

