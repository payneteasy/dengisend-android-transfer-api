/*
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 04/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

package com.payneteasy.dengisend.feedback;

import com.payneteasy.dengisend.AppModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        FeedbackModule.class,
})
public interface FeedbackComponent {

    void inject(FeedbackPresenter feedbackPresenter);
}