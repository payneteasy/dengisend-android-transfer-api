/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.transfer;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link TransferPresenter}.
 */
@Module
public class TransferPresenterModule {

    private final TransferContract.View mView;

    public TransferPresenterModule(TransferContract.View view) {
        mView = view;
    }

    @Provides
    TransferContract.View provideTransferContractView() {
        return mView;
    }

    @Provides
    TransferContract.Presenter provideTransferPresenter(TransferContract.View receiptsView, Context context) {
        return new TransferPresenter(receiptsView, context);
    }
}