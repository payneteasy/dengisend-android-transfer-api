/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.settings;

import dagger.Module;
import dagger.Provides;


/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link SettingsPresenter}.
 */
@Module
public class SettingsPresenterModule {

    private final SettingsContract.View mView;

    public SettingsPresenterModule(SettingsContract.View view) {
        mView = view;
    }

    @Provides
    SettingsContract.View provideSettingsContractView() {
        return mView;
    }

    @Provides
    SettingsContract.Presenter provideSettingsPresenter(SettingsContract.View receiptsView) {
        return new SettingsPresenter(receiptsView);
    }
}