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

package com.fede;

import android.os.Bundle;

public interface ServiceState {
	// handles an incoming sms stored in bundle from the service
	public void handleSms(HomeAloneService s, Bundle b);
	
	// handles an incoming call stored in bundle from the service
	public void handleIncomingCall(HomeAloneService s, Bundle b);
	
	// handles idle phone event
	public void handlePhoneIdle(HomeAloneService s);
	
	//handles phone off hook event
	public void handlePhoneOffHook(HomeAloneService s);
	
	// tells the value to store in the service state
	public boolean getServiceState();
}
