/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 03/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.receipts;

import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.data.source.ReceiptsRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        ReceiptsRepositoryModule.class,
})
public interface ReceiptsComponent {

    void inject(ReceiptsPresenter receiptsPresenter);
}