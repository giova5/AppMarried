package com.emis.appmarried.controller;

import com.emis.appmarried.model.UserProfileModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by jo5 on 17/05/18.
 */

public class DBManager {

    // always raise this number to TRIGGER a new migration
    private static final int MIGRATION_VERSION = 0;
    private static final String TAG = "DM_LOG";

    private static RealmConfiguration encryptedRealmConfiguration = null;

    public static void setupRealm(){
        getRealm();
    }

    private static Realm getRealm() {
        // Please remember that a separate migration module will be needed for the encrypted realm
        if (encryptedRealmConfiguration == null)
            encryptedRealmConfiguration = getSyncConfig();

        return Realm.getInstance(encryptedRealmConfiguration);
    }

    /**
     * This method is used to define a RealmConfiguration object to configure the Realm instance.
     * @return a RealmConfiguration object for the Realm instance configuration.
     * */
    private synchronized static RealmConfiguration getSyncConfig(){

        // The RealmConfiguration is created using the builder pattern.
        // The Realm file will be located in Context.getFilesDir() with name "encrypted.realm"

        if(encryptedRealmConfiguration == null) {
            encryptedRealmConfiguration = new RealmConfiguration
                    .Builder()
                    .name("encrypted.realm")
                    .schemaVersion(MIGRATION_VERSION) //Must be bumped when the schema changes
                    .build();
        }

        return encryptedRealmConfiguration;
    }

     /*
     * *************************************** Generic Realm Methods ***************************************
     **/


    /**
     * Generic method useful to add an Object (that extends RealmObject) to Realm.
     * @param object object to save
     */
    private static <T extends RealmObject> void addToRealm(T object){
        Realm realm = getRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * Generic method useful to get an Object (that extends RealmObject) from Realm.
     * @param clazz .class
     */
    private static <T extends RealmObject> List<T> getFromRealm(Class<T> clazz){
        Realm realm = getRealm();

        RealmResults<T> config = realm
                .where(clazz)
                .findAll();

        List<T> clone = (config.size() != 0) ? realm.copyFromRealm(config) : null;

        realm.close();

        return clone;
    }

    private  static <T extends RealmObject> void deleteFromRealm(String fieldName, String object, Class<T> objClass){
        Realm realm = getRealm();
        realm.beginTransaction();

        T realmObj = realm
                .where(objClass)
                .equalTo(fieldName, object)
                .findFirst();

        if(realmObj != null) {
            realmObj.deleteFromRealm();
        }
        realm.commitTransaction();
        realm.close();
    }

    /**
     * Generic method useful to get an object filtered on the field name passed as parameter.
     * */
    private static <T extends RealmObject> List<T> getFromRealmFilteredByString(String fieldName, String object, Class<T> clazz){
        Realm realm = getRealm();

        RealmResults<T> config = realm
                .where(clazz)
                .equalTo(fieldName, object)
                .findAll();

        List<T> clone = (config.size() != 0) ? realm.copyFromRealm(config) : null;

        realm.close();

        return clone;
    }

    /*
     * *************************************** Requests Section ***************************************
     **/

    public static void createUser(UserProfileModel user){
        addToRealm(user);
    }

    public static List<UserProfileModel> getUser(){
        return getFromRealm(UserProfileModel.class);
    }

}
