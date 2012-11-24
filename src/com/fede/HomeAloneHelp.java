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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class HomeAloneHelp extends SherlockActivity {
	SharedPreferences prefs;
    static final int HELP = 1;
    static final int LICENSE = 2;
    WebView mWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.help);
        
        mWebView = new WebView(this);
        setContentView(mWebView);
        mWebView.loadUrl("file:///android_asset/"+ getString(R.string.help_file_name));
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu sub = menu.addSubMenu("Help");
        sub.add(0, HELP, 0, R.string.help_name);
        sub.add(0, LICENSE, 0, R.string.license_name);
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
            return false;
        }
        switch(item.getItemId()){
            case HELP:
                mWebView.loadUrl("file:///android_asset/"+ getString(R.string.help_file_name));
            break;
            case LICENSE:
                mWebView.loadUrl("file:///android_asset/licenses.html");
            break;
        }
        return true;
    }
	
}
