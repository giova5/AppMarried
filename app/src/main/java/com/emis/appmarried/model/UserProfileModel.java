package com.emis.appmarried.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jo5 on 17/05/18.
 */

public class UserProfileModel extends RealmObject {

    @PrimaryKey
    private int userID;
    private String name;
    private String familyName;
    private String email;
    private String gender;
    private int age;
    private String picture;
    private String registrationDate;
    private String lastLoginDate;
    private String lastUpdateDate;
    private String birthDate;

    public UserProfileModel(){}

    public UserProfileModel(int userID, String name, String familyName, String email, String gender, int age, String picture, String registrationDate,
                            String lastLoginDate, String lastUpdateDate, String birthDate){
        this.userID = userID;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.picture = picture;
        this.registrationDate = registrationDate;
        this.lastLoginDate = lastLoginDate;
        this.lastUpdateDate = lastUpdateDate;
        this.birthDate = birthDate;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getBirdDate() {
        return birthDate;
    }

    public void setBirdDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
