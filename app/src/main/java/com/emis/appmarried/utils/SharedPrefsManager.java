package com.emis.appmarried.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for handling different user preferences of the application
 * Created by jo5 on 14/05/2017.
 */

public class SharedPrefsManager {
    private final static String SHARED_PREFENCE_NAME = "App_prefs";
    private final SharedPreferences sPrefs;

    SharedPrefsManager(Context ctx){
        sPrefs = ctx.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Return a String value saved in the shared preferences
     *
     * @param prefName the Shared preference name
     * @return The saved value or null if no preference is found
     */
    public String getStringPref(String prefName){
        return getStringPref(prefName, null);
    }

    /**
     * Return a String value saved in the shared preferences
     *
     * @param prefName the Shared preference name
     * @param defaultValue the default value to return if no preference is found
     * @return The saved value or the default argument if no preference is found
     */
    public String getStringPref(String prefName, String defaultValue){
        return sPrefs.getString(prefName, defaultValue);
    }

    public void putStringPref(String prefName, String value){
        sPrefs.edit().putString(prefName, value).apply();
    }

    /**
     * Return a boolean value saved in the shared preferences
     *
     * @param prefName the Shared preference name
     * @return The saved value or false if no preference is found
     */
    public boolean getBooleanPref(String prefName){
        return getBooleanPref(prefName, false);
    }

    /**
     * Return a boolean value saved in the shared preferences
     *
     * @param prefName the Shared preference name
     * @param defaultValue the default value to return if no preference is found
     * @return The saved value or the default argument if no preference is found
     */
    public boolean getBooleanPref(String prefName, boolean defaultValue){
        return sPrefs.getBoolean(prefName, defaultValue);
    }

    public void putBooleanPref(String prefName, boolean value){
        sPrefs.edit().putBoolean(prefName, value).apply();
    }

    /**
     * Method for clearing all keys saved in the applications shared preferences
     */
    public void clearAll(){
        sPrefs.edit().clear().apply();
    }
}
