/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 04/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.feedback;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class FeedbackModule {

    @Provides
    FeedbackContract.Presenter provideFeedbackPresenter(Context context) {
        return new FeedbackPresenter(context);
    }

    @Provides
    @NonNull
    @Singleton
    FeedbackContract.Service provideFeedbackService() {
        return FeedbackService.getInstance();
    }
}