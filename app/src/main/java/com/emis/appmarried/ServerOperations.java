package com.emis.appmarried;

import android.content.Context;
import android.content.Intent;

import com.emis.appmarried.controller.ServerRequestController;
import com.emis.appmarried.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.emis.appmarried.controller.ServerRequestController.POST_METHOD;
import static com.emis.appmarried.controller.ServerRequestController.RETRIES;
import static com.emis.appmarried.utils.Utils.EventType.GET_ACCESS_TOKEN;

/**
 * Created by jo5 on 16/05/18.
 */

public class ServerOperations {

    private URL url;
    private static String urlHeaderAsString = "https://www.emis-studios.com/webimaster-dev/api/v1/Webi/";

    private static String USERS = "Users/";
    private static String USER = "User/";


    private static String USERS_LOGIN = "Login/";
    private static String ACCESS_TOKEN = "AccessToken/";



    public ServerOperations(Utils.EventType eventType) {

        String currentEndPointForRequest;

        try {

            switch (eventType) {
                case USERS_LOGIN:
                    currentEndPointForRequest = urlHeaderAsString + USERS + USERS_LOGIN;
                    break;
                case GET_ACCESS_TOKEN:
                    currentEndPointForRequest = urlHeaderAsString + USERS + ACCESS_TOKEN;
                    break;
                default:
                    currentEndPointForRequest = null;
                    break;
            }

            url = new URL(currentEndPointForRequest);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    /*********************************** HTTP REQUESTS IMPLEMENTATION ************************************/

    public static void sendUserAuthenticate(Context context, String facebookAccessToken, MyResultReceiver callback){
        Intent smIntent = new Intent(context, ServerManagerService.class);
        smIntent.putExtra(ServerManagerService.COMMAND, ServerManagerService.COMMAND_USERS_LOGIN);
        smIntent.putExtra(ServerManagerService.PARAMETER_FB_ACCESS_TOKEN, facebookAccessToken);
        smIntent.putExtra(ServerManagerService.PARAMETER_RECEIVER_TAG, callback);
        context.startService(smIntent);
    }

    public JSONObject usersLogin(String facebookAccessToken) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonResponse = new JSONObject();

        try {
            jsonObject.put("accessTokenFacebook", facebookAccessToken);
            jsonResponse = sendRequest(Utils.EventType.USERS_LOGIN, POST_METHOD, jsonObject.toString(), RETRIES);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    public static void sendGetAccessToken(Context context, String refreshToken){
        Intent smIntent = new Intent(context, ServerManagerService.class);
        smIntent.putExtra(ServerManagerService.COMMAND, ServerManagerService.COMMAND_GET_ACCESS_TOKEN);
        smIntent.putExtra(ServerManagerService.PARAMETER_GET_ACCESS_TOKEN, refreshToken);
        context.startService(smIntent);
    }

    public JSONObject getAccessToken(String refreshToken) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonResponse = new JSONObject();

        try {
            jsonObject.put("WebiRefreshToken", refreshToken);
            jsonResponse = sendRequest(Utils.EventType.GET_ACCESS_TOKEN, POST_METHOD, jsonObject.toString(), RETRIES);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }



        /*********************************** SEND REQUEST METHOD ************************************/

    synchronized private JSONObject sendRequest(Utils.EventType eventType, String method, String jsonObject, int maxRetries) {

        String accessToken = null;

        //For requests with accessToken.

//        switch (eventType) {
//            case GET_ATTESTATION:
//                accessToken = JobApplication.getAccessToken();
//                break;
//        }

        ServerRequestController serverRequestManager = new ServerRequestController(eventType, method, jsonObject, maxRetries, accessToken);
        return serverRequestManager.sendRequest(url, accessToken);
    }

    synchronized private JSONObject sendGetRequestWithParams(Utils.EventType eventType, String method, String jsonObject, int maxRetries) {

        String accessToken = null;

        //For requests with accessToken.

//        switch (eventType) {
//            case GET_EXHIBITORS_INFO:
//                accessToken = JobApplication.getAccessToken();
//                break;
//        }

        ServerRequestController serverRequestManager = new ServerRequestController(eventType, method, jsonObject, maxRetries, accessToken);
        return serverRequestManager.sendGetRequestWithParams(url);
    }


}
