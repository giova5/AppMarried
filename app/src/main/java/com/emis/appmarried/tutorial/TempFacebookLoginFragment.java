package com.emis.appmarried.tutorial;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.emis.appmarried.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jo5 on 15/05/18.
 */

public class TempFacebookLoginFragment extends PageFragment{

    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.temp_facebook_login_fragment, container, false);

        loginButton = (LoginButton)root.findViewById(R.id.login_button);

        //If I am in a fragment, setFragment for facebook login implementation.
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FacebookFragment", "OK");
            }

            @Override
            public void onCancel() {
                Log.d("FacebookFragment", "KO onCancel()");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FacebookFragment", "KO onError()");
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
        Log.d("FacebookFragment", "onActivityResult() fragment");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
