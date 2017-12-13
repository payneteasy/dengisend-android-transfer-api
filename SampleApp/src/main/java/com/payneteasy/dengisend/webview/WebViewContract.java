package com.payneteasy.dengisend.webview;

import android.support.annotation.NonNull;

import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.MainContract;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 19/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface WebViewContract {

    interface View extends BaseView<Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);
    }

    interface Presenter extends BasePresenter {

    }
}
