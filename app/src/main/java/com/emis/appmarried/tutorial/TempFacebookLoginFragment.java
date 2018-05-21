package com.emis.appmarried.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.emis.appmarried.MyResultReceiver;
import com.emis.appmarried.R;
import com.emis.appmarried.ServerOperations;
import com.emis.appmarried.controller.ServerRequestController;
import com.emis.appmarried.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.emis.appmarried.ServerManagerService.API_EVENT_TYPE;
import static com.emis.appmarried.ServerManagerService.API_RESPONSE;

/**
 * Created by jo5 on 15/05/18.
 */

public class TempFacebookLoginFragment extends PageFragment implements MyResultReceiver.Receiver{

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private MyResultReceiver mReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        mReceiver = new MyResultReceiver(new Handler());

        mReceiver.setReceiver(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.temp_facebook_login_fragment, container, false);

        loginButton = (LoginButton)root.findViewById(R.id.login_button);

        //Login Button in a fragment.
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "OK");
                String facebookAccessToken = AccessToken.getCurrentAccessToken().getToken();
                ServerOperations.sendUserAuthenticate(getActivity(), facebookAccessToken, mReceiver);
            }

            @Override
            public void onCancel() {
                Log.d("Facebook", "KO onCancel()");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", "KO onError()");
            }
        });

        return root;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.temp_facebook_login_fragment;
    }

    @NonNull
    @Override
    protected TransformItem[] getTransformItems() {
        return new TransformItem[]{
                TransformItem.create(R.id.login_button, Direction.LEFT_TO_RIGHT, 0.2f),
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Facebook", "onActivityResult() fragment");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
//        Log.d("Facebook", "-------------" + resultData.getString(API_RESPONSE));

        try {
            JSONObject jsonObject = new JSONObject(resultData.getString(API_RESPONSE));
            Utils.EventType eventType = Utils.EventType.valueOf(resultData.getString(API_EVENT_TYPE));
            ServerRequestController.parseServerResponse(getActivity(), eventType, jsonObject, resultCode, mReceiver);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
