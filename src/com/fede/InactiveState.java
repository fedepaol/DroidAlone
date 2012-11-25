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

import com.fede.MessageException.InvalidCommandException;
import com.fede.Utilities.GeneralUtils;

public class InactiveState implements ServiceState {
	@Override
	public boolean getServiceState() {
		return false;
	}
	
	@Override
	public void handleIncomingCall(HomeAloneService s, Bundle b) {
		// if inactive returns 
	}

	@Override
	public void handlePhoneIdle(HomeAloneService s) {
		// if inactive returns 
	}

	
	@Override
	public void handleSms(HomeAloneService s, Bundle b) {
		String body = b.getString(HomeAloneService.MESSAGE_BODY);
		
		if(!CommandSms.isCommandSms(body)){
			return;
		}
		
		String number = b.getString(HomeAloneService.NUMBER);

		try{
			CommandSms command = new CommandSms(b, body, number, s);
			command.execute();
			
			if(command.getStatus() == CommandSms.BoolCommand.ENABLED){	
				s.setState(new ActiveState());
			}
		}catch (InvalidCommandException p){
			// in case of failure the user must be notified anyway
			GeneralUtils.sendSms(number, p.getMessage(), s);
		}
		
	}

	@Override
	public void handlePhoneOffHook(HomeAloneService s) {
		// TODO Auto-generated method stub
		
	}

}
