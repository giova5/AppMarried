package com.emis.appmarried;

import android.app.Application;

import com.emis.appmarried.controller.DBManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.realm.Realm;

/**
 * Created by jo5 on 15/05/18.
 */

public class AppMarriedApplication extends Application {

    private static String accessToken;
    private static String refreshToken;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize realm DB
        Realm.init(this);
        DBManager.setupRealm();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        AppMarriedApplication.accessToken = accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        AppMarriedApplication.refreshToken = refreshToken;
    }
}
