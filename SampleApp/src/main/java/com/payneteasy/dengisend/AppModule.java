/*
 * Dengisend
 *
 * Created by Alex Oleynyak on 02/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.payneteasy.dengisend.transfer.TransferService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module
 */
@Module
public class AppModule {

    @NonNull
    private final DengisendApplication application;

    public AppModule(@NonNull DengisendApplication application) {
        this.application = application;
    }

    @Provides
    @NonNull
    @Singleton
    public Context provideContext() {
        return application.getApplicationContext();
    }
}