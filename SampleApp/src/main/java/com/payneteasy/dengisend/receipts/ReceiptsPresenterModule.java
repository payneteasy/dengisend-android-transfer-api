/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receipts;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link ReceiptsPresenter}.
 */
@Module
public class ReceiptsPresenterModule {

    private final ReceiptsContract.View mView;

    public ReceiptsPresenterModule(ReceiptsContract.View view) {
        mView = view;
    }

    @Provides
    ReceiptsContract.View provideReceiptsContractView() {
        return mView;
    }

    @Provides
    ReceiptsContract.Presenter provideReceiptsPresenter(ReceiptsContract.View receiptsView, Context context) {
        return new ReceiptsPresenter(receiptsView, context);
    }
}