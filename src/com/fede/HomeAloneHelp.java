package com.fede;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

public class HomeAloneHelp extends Activity{
	SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.help);
        
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.loadUrl("file:///android_asset/"+ getString(R.string.help_file_name));
	}
	
	
}
