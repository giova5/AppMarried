package com.emis.appmarried;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by jo5 on 16/05/18.
 */

public class MyResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public MyResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
