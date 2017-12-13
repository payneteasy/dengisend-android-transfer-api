/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import com.payneteasy.dengisend.data.source.local.ReceiptsLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * This is used by Dagger to inject the required arguments into the {@link ReceiptsRepository}.
 */
@Module
public class ReceiptsRepositoryModule {

    @Provides
    @NonNull
    @Singleton
    ReceiptsDataSource provideReceiptsLocalDataSource(@NonNull Context context){
        return new ReceiptsLocalDataSource(context);
    }

    @Provides
    @NonNull
    @Singleton
    ReceiptsRepository provideReceiptsRepository(@NonNull ReceiptsDataSource receiptsLocalDataSource){
        return new ReceiptsRepository(receiptsLocalDataSource);
    }
}

