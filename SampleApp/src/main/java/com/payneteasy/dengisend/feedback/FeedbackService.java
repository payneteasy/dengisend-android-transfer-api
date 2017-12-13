package com.payneteasy.dengisend.feedback;

import android.util.Log;

import com.payneteasy.dengisend.BaseService;
import com.payneteasy.dengisend.api.DengisendApiCallback;
import com.payneteasy.dengisend.transfer.TransferContract;

import java.util.List;
import java.util.Map;

import com.payneteasy.android.ApiException;
import com.payneteasy.android.model.AccessTokenResponse;
import com.payneteasy.android.model.SendFeedbackRequest;
import com.payneteasy.android.model.SendFeedbackResponse;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 13/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class FeedbackService extends BaseService implements FeedbackContract.Service {

    private static FeedbackContract.Service INSTANCE = null;

    public FeedbackService() {

        super();
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link FeedbackService} instance
     */
    public static FeedbackContract.Service getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FeedbackService();
        }
        return INSTANCE;
    }

    @Override
    public void sendFeedback(final String orderId, final String email, final String message, final FeedbackContract.SendFeedbackCallback sendFeedbackCallback) {

        requestAccessToken(new TransferContract.RequestAccessTokenCallback() {
            @Override
            public void onFailure(String errorMessage) {

            }

            @Override
            public void onSuccess(AccessTokenResponse result) {

                SendFeedbackRequest feedbackRequest = new SendFeedbackRequest();
                feedbackRequest.setEmail(email);
                feedbackRequest.setMessage(message);
                feedbackRequest.setOrderId(orderId);
                feedbackRequest.setSession(session);

                try {
                    merchantApi.supportSendMessagePostAsync(feedbackRequest, new DengisendApiCallback<SendFeedbackResponse>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            Log.d("SEND FEEDBACK ERROR", e.getMessage());
                            sendFeedbackCallback.onFailure(e.getMessage());
                        }

                        @Override
                        public void onSuccess(SendFeedbackResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                            sendFeedbackCallback.onSuccess(result);
                        }
                    });
                } catch (ApiException apiException) {
                    Log.d("SEND FEEDBACK ERROR", apiException.getMessage());
                    sendFeedbackCallback.onFailure(apiException.getMessage());
                }
            }
        });
    }
}
