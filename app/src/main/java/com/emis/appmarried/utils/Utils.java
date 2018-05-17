package com.emis.appmarried.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jo5 on 16/05/18.
 */

public class Utils {

    private static String SHARED_SECRET = "SECIRIUSCIAMOSIAMODEGLIDEISISISI";

    public enum EventType {
        USERS_LOGIN,
        GET_ACCESS_TOKEN,
        GET_USER_PROFILE
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(SHARED_SECRET.getBytes(), "AES/CBC/PKCS5Padding");
    }

    @Nullable
    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        Cipher cipher = null;
        byte[] newEncryptedBytes;

        try {
            /* Encrypt the message. */
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Creating IV
            SecureRandom r = new SecureRandom();
            byte[] ivBytes = new byte[16];
            r.nextBytes(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivBytes));

            byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));

            newEncryptedBytes = new byte[encryptedBytes.length + ivBytes.length];

            System.arraycopy(ivBytes, 0, newEncryptedBytes, 0, ivBytes.length);
            System.arraycopy(encryptedBytes, 0, newEncryptedBytes, ivBytes.length, encryptedBytes.length);
        }

        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }

        return newEncryptedBytes;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        // Get Connectivity Manager class object from Systems Service
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Network Info from connectivity Manager
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

}