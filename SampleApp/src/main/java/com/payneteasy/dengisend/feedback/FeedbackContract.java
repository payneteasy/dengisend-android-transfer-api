package com.payneteasy.dengisend.feedback;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.payneteasy.dengisend.BasePresenter;
import com.payneteasy.dengisend.BaseView;
import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.domain.model.Receipt;

import com.payneteasy.android.model.SendFeedbackResponse;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 12/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public interface FeedbackContract {

    interface View extends BaseView<Presenter> {

        void setMainActivity(@NonNull MainContract.Activity activity);

        EditText getFeedbackEmail();

        EditText getFeedbackMessage();

        void setFeedbackTitleForReceipt(String orderId, String date);

        void setReadOnlyTransferUI(boolean readonly);

        void startProgress();

        void stopProgress();

        void showAlertDialog(String message, boolean goBackToReceipt);

        void customizeUI();
    }

    interface Presenter extends BasePresenter {

        void setFeedbackView(FeedbackContract.View feedbackView);

        void setReceipt(@Nullable Receipt receipt);

        void readSettings();

        void writeSettings();

        void saveDraft();

        void restoreFromDraft();

        void sendFeedback();
    }

    interface Service {

        void sendFeedback(String orderId, String email, String message, final FeedbackContract.SendFeedbackCallback sendFeedbackCallback);
    }

    interface SendFeedbackCallback {

        void onFailure(String errorMessage);

        void onSuccess(SendFeedbackResponse result);
    }
}
