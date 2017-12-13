package com.payneteasy.dengisend.settings;

import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 08/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mSettingsView;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    SettingsPresenter(SettingsContract.View settingsView) {
        mSettingsView = settingsView;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mSettingsView.setPresenter(this);
    }

    @Override
    public void start() {
        // show form
    }
}
