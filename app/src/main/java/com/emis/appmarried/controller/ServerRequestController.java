package com.emis.appmarried.controller;

/**
 * Created by jo5 on 16/05/18.
 */

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.emis.appmarried.AppMarriedApplication;
import com.emis.appmarried.MyResultReceiver;
import com.emis.appmarried.ServerOperations;
import com.emis.appmarried.model.UserProfileModel;
import com.emis.appmarried.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by jo5 on 17/10/17.
 */

public class ServerRequestController {

    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String PUT_METHOD = "PUT";
    public static final String DELETE_METHOD = "DELETE";
    public static final int RETRIES = 3;
    private final static int CONNECTION_REFUSED = -999;
    private final static int SERVER_ERRORS = 500;

    private Utils.EventType requestName;
    private String method;
    private String body;
    private int maxRetries;
    private HttpURLConnection conn;
    private int responseCode;
    private int currentRetry = 3;
    private JSONObject jsonResponse;
    private String accessToken;

    public ServerRequestController(Utils.EventType requestName, String method, String body, int maxRetries, String accessToken){
        this.requestName = requestName;
        this.method = method;
        this.body = body;
        this.maxRetries = maxRetries;
        this.accessToken = accessToken;
    }


    @Nullable
    private static HttpURLConnection getHTTPConnectionObject(URL url) {

        HttpURLConnection cxn;
        try {
            cxn = (HttpURLConnection) url.openConnection();
            return cxn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject performConnection(URL url, String accessToken) {

        Log.d("ServerRequestController", "Request Body -- " + body);

        try {

            conn = getHTTPConnectionObject(url);

            if(method.equals(POST_METHOD)) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }

            conn.setRequestMethod(method);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(5000);

            if (accessToken != null)
                conn.setRequestProperty("Authorization", accessToken);

            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();

            if(body != null)
                writeOutputStream();

            responseCode = conn.getResponseCode();

            Log.d("ServerRequestControl","Request response code: " + String.valueOf(responseCode));

            String response = receivedResponse();
            if(response == null)
                return null;

            Log.d("ServerRequestController", "Request response: " + response);

            if (responseCode >= SERVER_ERRORS)
                retry(url);

            jsonResponse = (!response.isEmpty()) ? new JSONObject(response) : new JSONObject();
            jsonResponse.put("responseCode", responseCode);

        }catch(java.net.SocketTimeoutException e) {
            e.printStackTrace();
            retry(url);
        }catch (UnknownHostException e){
            retry(url);
        } catch (IOException e) {
            try {
                jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", CONNECTION_REFUSED);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (JSONException e) {
            try {
                jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", CONNECTION_REFUSED);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return jsonResponse;
    }

    public JSONObject performGetWithParamsConnection(URL url){

        Log.d("ServerRequestController", "Request Body -- " + body);
        String getUrl = null;

        try {

            JSONObject jsonObject = new JSONObject(body);

            if(requestName.equals(Utils.EventType.GET_ACCESS_TOKEN))
                getUrl = url.toString() + "?refresh_token=" + jsonObject.getString("refresh_token");

            conn = getHTTPConnectionObject(new URL(getUrl));

            if (accessToken != null)
                conn.setRequestProperty("Authorization", accessToken);

            conn.setRequestMethod(method);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(5000);

            conn.connect();

            responseCode = conn.getResponseCode();

            Log.d("ServerRequestControl","Request response code: " + String.valueOf(responseCode));

            String response = receivedResponse();
            if(response == null)
                return null;

            Log.d("ServerRequestController", "Request response: " + response);

            if (responseCode >= SERVER_ERRORS)
                retry(url);

            jsonResponse = (!response.isEmpty()) ? new JSONObject(response) : new JSONObject();
            jsonResponse.put("responseCode", responseCode);

        }catch(java.net.SocketTimeoutException e) {
            e.printStackTrace();
            retry(url);
        }catch (UnknownHostException e){
            retry(url);
        } catch (IOException e) {
            try {
                jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", CONNECTION_REFUSED);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (JSONException e) {
            try {
                jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", CONNECTION_REFUSED);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return jsonResponse;
    }

    public JSONObject sendRequest(URL url, String accessToken){

        return performConnection(url, accessToken);

    }

    public JSONObject sendGetRequestWithParams(URL url){
        return performGetWithParamsConnection(url);
    }


    private void writeOutputStream(){
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String receivedResponse() throws IOException {
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        // retrieve the response from server
        InputStream is = null;
        InputStreamReader isr = null;

        try {
            if(responseCode == 200 || responseCode == 201 || responseCode == 202 || responseCode == 206)
                is = conn.getInputStream();
            else
                is = conn.getErrorStream();

            if(is == null)
                return null;

            isr = new InputStreamReader(is, "UTF-8");

            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = isr.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (isr != null) {
                isr.close();
            }
        }
    }

    private void retry(URL url){
        if (currentRetry <= maxRetries - 1) {
            Log.d("ServerRequestController", "Retry number " + currentRetry);
            SystemClock.sleep(2 ^ currentRetry * 1000);
            currentRetry++;
            performConnection(url, accessToken);
        }
    }

    public static void parseServerResponse(Context context, Utils.EventType eventType, JSONObject jsonResponse, int responseCode, MyResultReceiver callback) {

        try {
            switch (eventType){
                case USERS_LOGIN:
                    if(responseCode == 200 || responseCode == 201) {
                        JSONObject webObj = jsonResponse.getJSONObject("WebiRefreshToken");
                        String refreshToken = webObj.getString("token");
                        AppMarriedApplication.setRefreshToken(refreshToken);
                        ServerOperations.sendGetAccessToken(context, refreshToken, callback);
                    }
                    break;
                case GET_ACCESS_TOKEN:
                    if(responseCode == 200 || responseCode == 201) {
                        JSONObject webObj = jsonResponse.getJSONObject("WebiAccessToken");
                        String accessToken = webObj.getString("token");
                        AppMarriedApplication.setAccessToken(accessToken);
                        ServerOperations.sendGetUserProfile(context, callback);
                    }
                    break;
                case GET_USER_PROFILE:
                    if(responseCode == 200 || responseCode == 201) {
                        JSONObject webObj = jsonResponse.getJSONObject("UserProfile");
                        int userID = webObj.getInt("userId");
                        String name = webObj.getString("name");
                        String familyName = webObj.getString("familyName");
                        String email = webObj.getString("email");
                        String gender = webObj.getString("gender");
                        int age = webObj.getInt("age");
                        String picture = webObj.getString("picture");
                        String registrationDate = webObj.getString("registrationDate");
                        String lastLoginDate = webObj.getString("lastLoginDate");
                        String lastUpdateDate = webObj.getString("lastUpdateDate");
                        String birthDate = webObj.getString("birthDate");

                        UserProfileModel userProfile = new UserProfileModel(userID, name, familyName, email, gender, age, picture, registrationDate, lastLoginDate,
                                lastUpdateDate, birthDate);

                        DBManager.createUser(userProfile);
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}