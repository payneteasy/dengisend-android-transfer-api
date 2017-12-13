/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 04/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receiptdetails;

import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.data.source.ReceiptsRepositoryModule;
import com.payneteasy.dengisend.transfer.TransferModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        TransferModule.class,
        ReceiptsRepositoryModule.class,
        ReceiptDetailsPresenterModule.class,
})
public interface ReceiptDetailsComponent {

    void inject(ReceiptDetailsPresenter receiptDetailsPresenter);
}