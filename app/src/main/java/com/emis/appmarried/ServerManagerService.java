package com.emis.appmarried;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.emis.appmarried.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jo5 on 16/05/18.
 */

public class ServerManagerService extends IntentService {



    public interface ServerManagerResp{
        void onResponseReceived(Utils.EventType eventType, int response, JSONObject jsonObject);
    }

    private static final String TAG = "ServerManagerService";

    private ServerManagerResp listener;


    public static final String RESPONSE_CODE = "responseCode";
    public static final int NO_INTERNET_CONNECTION = -999;
    public static final String OPERATION_SUCCESS_200 = "200";
    public static final String OPERATION_FAILURE_401 = "401";

    //commands switch case
    public static final String COMMAND = "COMMAND";
    public static final String COMMAND_USERS_LOGIN = "COMMAND_USERS_LOGIN";
    public static final String COMMAND_GET_ACCESS_TOKEN = "COMMAND_GET_ACCESS_TOKEN";

    //Requests parameters
    public static final String PARAMETER_FB_ACCESS_TOKEN = "PARAMETER_FB_ACCESS_TOKEN";
    public static final String PARAMETER_GET_ACCESS_TOKEN = "PARAMETER_GET_ACCESS_TOKEN";

    private int currentResponseCode;
    private JSONObject currentResponse;
    private Utils.EventType currentEventType;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServerManagerService(String name) {
        super(name);
    }

    public ServerManagerService(){
        super("ServerManagerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) {
            Log.d(TAG, "onHandleIntent called with null intent, probably has been restarted...");
            // Without params -> do nothing
            return;
        }

        String command = intent.getStringExtra(COMMAND);

        switch (command){
            case COMMAND_USERS_LOGIN:
                String facebookAccessToken = intent.getStringExtra(PARAMETER_FB_ACCESS_TOKEN);

                try {
                    ServerOperations usersLogin = new ServerOperations(Utils.EventType.USERS_LOGIN);
                    JSONObject usersLoginResponse = usersLogin.usersLogin(facebookAccessToken);
                    usersLoginResponse = checkIfInitResponse(usersLoginResponse);
                    currentResponse = usersLoginResponse;
                    currentResponseCode = Integer.valueOf(currentResponse.getString(RESPONSE_CODE));
                    currentEventType = Utils.EventType.USERS_LOGIN;
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case COMMAND_GET_ACCESS_TOKEN:
                String refreshToken = intent.getStringExtra(PARAMETER_GET_ACCESS_TOKEN);
                try {
                    ServerOperations getAccessTokenRequest = new ServerOperations(Utils.EventType.GET_ACCESS_TOKEN);
                    JSONObject getAccessTokenResponse = getAccessTokenRequest.getAccessToken(refreshToken);
                    currentResponse = checkIfInitResponse(getAccessTokenResponse);
                    currentResponseCode = getIntFromString(currentResponse.getString(RESPONSE_CODE));
                    currentEventType = Utils.EventType.GET_ACCESS_TOKEN;
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                currentResponse = null;
                currentResponseCode = -1;
                currentEventType = null;
                break;
        }

        if(listener != null) {
            listener.onResponseReceived(currentEventType, currentResponseCode, currentResponse);
        }
    }

    /**
     * This method create a jsonObject if response is null.
     * */
    private JSONObject checkIfInitResponse(JSONObject jsonResponse){
        try {
            if (jsonResponse == null) {
                jsonResponse = new JSONObject();
                jsonResponse.put(RESPONSE_CODE, NO_INTERNET_CONNECTION);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setServerManagerRespListener(ServerManagerResp listener) {
        this.listener = listener;
    }

    private int getIntFromString(String str){
        return Integer.valueOf(str);
    }

}
