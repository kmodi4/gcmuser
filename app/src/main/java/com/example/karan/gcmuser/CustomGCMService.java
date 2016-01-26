package com.example.karan.gcmuser;

import android.os.Bundle;

import co.mobiwise.fastgcm.GCMListenerService;

public class CustomGCMService extends GCMListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        //Here is called even app is not working.
        //create your notification here.
    }
}
