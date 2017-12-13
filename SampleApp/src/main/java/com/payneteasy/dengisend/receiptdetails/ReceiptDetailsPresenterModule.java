/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 04/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receiptdetails;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


@Module
public class ReceiptDetailsPresenterModule {

    @Provides
    ReceiptDetailsContract.Presenter provideReceiptDetailsPresenter(Context context) {
        return new ReceiptDetailsPresenter(context);
    }
}