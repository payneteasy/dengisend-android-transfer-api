/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 01/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.transfer;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TransferModule {

    @Provides
    @NonNull
    @Singleton
    public TransferContract.Service provideTransferService(Context context) {
        return TransferService.getInstance(context);
    }
}
