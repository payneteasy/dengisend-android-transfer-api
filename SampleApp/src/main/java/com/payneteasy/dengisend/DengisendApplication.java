package com.payneteasy.dengisend;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class DengisendApplication extends Application {

    @Nullable
    private AppComponent appComponent;

    @NonNull
    public static DengisendApplication get(@NonNull Context context) {
        return (DengisendApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //appComponent();
    }

    @NonNull
    public AppComponent appComponent() {
        if (appComponent == null) {
            synchronized (DengisendApplication.class) {
                if (appComponent == null) {
                    appComponent = createAppComponent();
                }
            }
        }

        return appComponent;
    }

    @NonNull
    private AppComponent createAppComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}