package com.fede.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.fede.R;
import com.fede.MessageException.LocationNotFoundException;



public class LocationUpdater {
	private LocationManager mLManager;
	private String mLProvider;
	private Context mContext;
	private final Object mLock = new Object();
	
	public Location getLocation() throws LocationNotFoundException{
		Location location = null;
		
		LocationListener l = new LocationListener() {
	        public void onLocationChanged(Location location) {
	        	location =  mLManager.getLastKnownLocation(mLProvider);
	            synchronized (mLock) {
	            	mLock.notify();
	            }
	        }
       
	        public void onProviderDisabled(String provider){
	        		        	
	        }
	        
	        public void onStatusChanged(String provider, int status, Bundle extras) {
	        }

	        public void onProviderEnabled(String provider) {}
		};
		
		mLManager.requestLocationUpdates(mLProvider, 1000, 0, l);
		synchronized(mLock){
			try{
				mLock.wait();
			}catch(InterruptedException e){
			}
		}
		
		mLManager.removeUpdates(l);
		if(location == null){
			throw new  LocationNotFoundException(mContext.getString(R.string.location_not_found), mContext);
		}
		
	
		List<Address> addresses = null;
		Geocoder gc = new Geocoder(mContext, Locale.getDefault());
		try {
			addresses = gc.getFromLocation(location.getLatitude(),
				  						location.getLongitude(), 2);
		} catch (IOException e) {
			//TODO
		}
		if (addresses.size() >0){
			Address a = addresses.get(0);
		}
		
		
		return location;
	}
	
	
	
	public LocationUpdater(Context c)
	{
		
		mLManager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
		mLProvider = LocationManager.GPS_PROVIDER;
		mContext = c;
        

	    
	}
	
	
};
