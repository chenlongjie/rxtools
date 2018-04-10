package com.vondear.tools;

import android.app.Application;

import com.vondear.rxtools.RxUtils;

import okhttp3.OkHttpClient;

/**
 * Created by vonde on 2016/12/23.
 */

public class ApplicationRxTools extends Application {
    public static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        RxUtils.init(this);
    }

}
