package com.payneteasy.dengisend.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.R;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.utils.Strings;

import javax.inject.Inject;

import com.payneteasy.android.model.SendFeedbackResponse;


/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 12/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class FeedbackPresenter implements FeedbackContract.Presenter {

    private FeedbackContract.View mFeedbackView;

    private Context mContext;

    private SharedPreferences sharedPreferences;

    @Nullable
    Receipt mReceipt;

    @Inject
    FeedbackContract.Service feedbackService;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    FeedbackPresenter(Context context) {
        this.mContext = context;

        DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule())
                .build()
                .inject(this);
    }

    @Override
    public void setFeedbackView(FeedbackContract.View feedbackView) {
        mFeedbackView = feedbackView;
    }

    @Override
    public void setReceipt(@Nullable Receipt receipt) {
        this.mReceipt = receipt;
    }

    @Override
    public void start() {

        if (mReceipt != null) {
            mFeedbackView.setFeedbackTitleForReceipt(mReceipt.getOrderId(), Strings.longStringFromDateTime(mReceipt.getDate()));
        }

        if (mContext != null) {

            sharedPreferences = mContext.getSharedPreferences(Config.SHARED_PREF_FILE, 0);

            restoreFromDraft();
        }
    }

    @Override
    public void readSettings() {

        if (sharedPreferences == null) {
            return;
        }

        String email = sharedPreferences.getString(Config.SHARED_PREF_PARAM_USER_EMAIL, "");

        if (email.length() > 0) {
            mFeedbackView.getFeedbackEmail().setText(email);
        }
    }

    @Override
    public void writeSettings() {

        if (sharedPreferences == null) {
            return;
        }

        String email = mFeedbackView.getFeedbackEmail().getText().toString();

        if (email.length() > 0) {
            sharedPreferences.edit()
                    .putString(Config.SHARED_PREF_PARAM_USER_EMAIL, email)
                    .apply();
        }
    }

    @Override
    public void restoreFromDraft() {

        readSettings();
    }

    @Override
    public void saveDraft() {

        writeSettings();
    }

    @Override
    public void sendFeedback() {

        mFeedbackView.setReadOnlyTransferUI(true);
        mFeedbackView.startProgress();

        FeedbackContract.SendFeedbackCallback sendFeedbackCallback = new FeedbackContract.SendFeedbackCallback() {
            @Override
            public void onFailure(String errorMessage) {
                mFeedbackView.showAlertDialog(errorMessage, false);
                mFeedbackView.setReadOnlyTransferUI(false);
                mFeedbackView.stopProgress();
            }

            @Override
            public void onSuccess(SendFeedbackResponse result) {
                mFeedbackView.showAlertDialog(mContext.getResources().getString(R.string.feedback_successfully_sent), true);
                mFeedbackView.setReadOnlyTransferUI(false);
                mFeedbackView.stopProgress();
            }
        };

        feedbackService.sendFeedback(
                (mReceipt != null) ? mReceipt.getOrderId() : "",
                mFeedbackView.getFeedbackEmail().getText().toString(),
                mFeedbackView.getFeedbackMessage().getText().toString(),
                sendFeedbackCallback
        );
    }
}
