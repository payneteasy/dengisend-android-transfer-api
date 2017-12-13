package com.payneteasy.dengisend;

import android.util.Log;

import java.util.List;
import java.util.Map;

import com.payneteasy.dengisend.api.DengisendApiCallback;
import com.payneteasy.dengisend.transfer.TransferContract;

import com.payneteasy.android.ApiClient;
import com.payneteasy.android.ApiException;
import com.payneteasy.android.api.DefaultApi;
import com.payneteasy.android.model.AccessTokenResponse;
import com.payneteasy.android.model.Session;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 14/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class BaseService {

    protected DefaultApi merchantApi;

    protected Session session;

    public BaseService() {

        session = new Session();

        // MERCHANT API
        ApiClient merchantApiClient = new ApiClient();
        merchantApiClient.setBasePath(Config.MERCHANT_BASE_ADDRESS);
        merchantApi = new DefaultApi(merchantApiClient);
    }

    protected void requestAccessToken(final TransferContract.RequestAccessTokenCallback requestAccessTokenCallback) {

        try {
            merchantApi.authBankIdRequestAccessTokenGetAsync(Config.DENGISEND_IDENTIFIER, new DengisendApiCallback<AccessTokenResponse>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    Log.d("ACCESS TOKEN FAILURE", e.getMessage());
                    requestAccessTokenCallback.onFailure(e.getMessage());
                }

                @Override
                public void onSuccess(AccessTokenResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                    Log.d("ACCESS TOKEN OK", result.getSession().getAccessToken());

                    session.setAccessToken(result.getSession().getAccessToken());

                    requestAccessTokenCallback.onSuccess(result);
                }
            });

        } catch (ApiException apiException) {
            Log.d("ACCESS TOKEN Exception", apiException.getMessage());
        }
    }
}
