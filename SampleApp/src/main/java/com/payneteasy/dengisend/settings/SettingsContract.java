package com.payneteasy.dengisend.settings;

import android.support.annotation.NonNull;

import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.MainContract;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 07/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);

        void customizeUI();

        void onViewVisible();
    }

    interface Presenter extends BasePresenter {

    }
}
