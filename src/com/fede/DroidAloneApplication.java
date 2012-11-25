package com.fede;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 11/25/12
 * Time: 3:03 PM
 */
@ReportsCrashes(formKey = "dFp4ZFhCaFRMS01pUkhEQ2pFajdMSWc6MQ")
public class DroidAloneApplication extends Application {
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
}
