package com.emis.appmarried;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by jo5 on 15/05/18.
 */

public class AppMarriedApplication extends Application {

    private static String accessToken;

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        AppMarriedApplication.accessToken = accessToken;
    }
}
