/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

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
import android.os.SystemClock;

import com.fede.R;
import com.fede.MessageException.LocationNotFoundException;



public class LocationUpdater {
	private LocationManager mLManager;
	private String mLProvider;
	private Context mContext;
	static final long maxAgeNetworkMilliSeconds = 1000 * 60 * 10;  // 10 minutes
	
	private Location findLocation(int sleepTime){
		SystemClock.sleep(sleepTime);
		Location here = mLManager.getLastKnownLocation(mLProvider);		
				
		long now = System.currentTimeMillis();
		long locationTime = here.getTime();
		
		if (here == null || locationTime < now - maxAgeNetworkMilliSeconds){
			return null;
		}
		return here;
	}
	
	private String locationFromCoordinates(Location l){
		return "lat " + String.valueOf(l.getLatitude()) + " lon " + String.valueOf(l.getLongitude());
	}
	
	public String getLocation() throws LocationNotFoundException{

		LocationListener l = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				String fava = "fava";
				
			}

			@Override
			public void onProviderDisabled(String provider){}
			
			@Override
			public void onProviderEnabled(String provider) {
				String fava = "fava";
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {}	
		};
		
		mLManager.requestLocationUpdates(mLProvider, 1000, 0, l);

		Location here = findLocation(5000);
		
		if(here == null){	// I try again
			here = findLocation(5000);	
		}
		if(here == null){
			throw new LocationNotFoundException(mContext.getString(R.string.location_not_found), mContext);
		}
		
		mLManager.removeUpdates(l);		
	
		List<Address> addresses = null;
		Geocoder gc = new Geocoder(mContext, Locale.getDefault());
		try {
			addresses = gc.getFromLocation(here.getLatitude(),
				  						here.getLongitude(), 2);
			if (addresses.size() == 0){				
				SystemClock.sleep(1000);
				addresses = gc.getFromLocation(here.getLatitude(),
						here.getLongitude(), 2);
			}
			
		} catch (IOException e) {
			 return locationFromCoordinates(here);
		}
		if (addresses.size() == 0){
			return locationFromCoordinates(here);			
		}
		
		Address a = addresses.get(0);
		
		
		String res = String.format("%s - %s-%s-%s-%s", locationFromCoordinates(here), a.getCountryName(), a.getLocality(), a.getAddressLine(0), a.getFeatureName());
		return res;
	}
	
	
	
	public LocationUpdater(Context c) throws LocationNotFoundException
	{		
		mLManager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
		if(!mLManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			throw new LocationNotFoundException(c.getString(R.string.location_not_found), c);
		}

		mLProvider = LocationManager.NETWORK_PROVIDER;
		mContext = c;	    
	}
	
	
};
