/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 01/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.transfer;

import com.payneteasy.dengisend.AppModule;
import com.payneteasy.dengisend.data.source.ReceiptsRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {
                AppModule.class,
                TransferModule.class,
                ReceiptsRepositoryModule.class,
        }
)
public interface TransferComponent {

    void inject(TransferPresenter transferPresenter);
}