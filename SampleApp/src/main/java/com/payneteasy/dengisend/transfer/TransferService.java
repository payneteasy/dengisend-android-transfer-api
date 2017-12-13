package com.payneteasy.dengisend.transfer;

import android.content.Context;
import android.util.Log;

import com.payneteasy.dengisend.BaseService;
import com.payneteasy.dengisend.Config;
import com.payneteasy.dengisend.api.DengisendApiCallback;
import com.payneteasy.dengisend.domain.model.DestinationCard;
import com.payneteasy.dengisend.domain.model.Receipt;
import com.payneteasy.dengisend.domain.model.SourceCard;
import com.payneteasy.dengisend.domain.model.Transfer;
import com.payneteasy.dengisend.utils.ActivityUtils;
import com.payneteasy.dengisend.utils.TransferStatus;

import java.util.List;
import java.util.Map;

import com.payneteasy.android.ApiClient;
import com.payneteasy.android.ApiException;
import com.payneteasy.android.api.DefaultApi;
import com.payneteasy.android.model.AccessTokenResponse;
import com.payneteasy.android.model.CardsIdsRequest;
import com.payneteasy.android.model.CardsIdsResponse;
import com.payneteasy.android.model.Consumer;
import com.payneteasy.android.model.ConsumerDevice;
import com.payneteasy.android.model.InitiateTransferRequest;
import com.payneteasy.android.model.InitiateTransferResponse;
import com.payneteasy.android.model.PerformTransferRequest;
import com.payneteasy.android.model.PerformTransferResponse;
import com.payneteasy.android.model.RatesRequest;
import com.payneteasy.android.model.RatesResponse;
import com.payneteasy.android.model.Session;
import com.payneteasy.android.model.Transaction;
import com.payneteasy.android.model.TransferStatusRequest;
import com.payneteasy.android.model.TransferStatusResponse;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 31/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class TransferService extends BaseService implements TransferContract.Service {

    private static TransferService INSTANCE = null;

    private Consumer consumer;

    private DefaultApi paynetEasyApi;

    private long _updateInterval = 1000;

    private TransferService(Context context) {

        ConsumerDevice device = new ConsumerDevice();
        device.setSerialNumber(ActivityUtils.getSerial(context));

        session = new Session();

        consumer = new Consumer();
        consumer.setDevice(device);

        // PNE API
        ApiClient paynetEasyApiClient = new ApiClient();
        paynetEasyApiClient.setBasePath(Config.PAYNET_BASE_ADDRESS);
        paynetEasyApi = new DefaultApi(paynetEasyApiClient);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param context the Context
     * @return the {@link TransferService} instance
     */
    public static TransferService getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TransferService(context);
        }
        return INSTANCE;
    }

    @Override
    public void clearSession() {
        if (session != null){
            session.setAccessToken(null);
        }
    }

    @Override
    public void requestCommission(final TransferContract.RequestCommissionCallback requestCommissionCallback) {

        if (session == null || session.getAccessToken() == null) {

            requestAccessToken(new TransferContract.RequestAccessTokenCallback() {
                @Override
                public void onFailure(String errorMessage) {
                    requestCommissionCallback.onFailure(errorMessage);
                }

                @Override
                public void onSuccess(AccessTokenResponse result) {

                    session.setAccessToken(result.getSession().getAccessToken());

                    requestCommission(requestCommissionCallback);
                }
            });

        } else {

            if (session.getAccessToken() != null) {

                RatesRequest sessionData = new RatesRequest();
                sessionData.setConsumer(consumer);
                sessionData.setSession(session);

                try {

                    merchantApi.configGetRatePostAsync(sessionData, new DengisendApiCallback<RatesResponse>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            Log.d("GET RATE Failure", e.getMessage());

                            requestCommissionCallback.onFailure(e.getMessage());
                        }

                        @Override
                        public void onSuccess(RatesResponse result, int statusCode, Map<String, List<String>> responseHeaders) {

                            if (result.getError() != null) {
                                Log.d("GET RATE ERROR", result.getError().toString());

                                requestCommissionCallback.onFailure(result.getError().toString());
                            } else {
                                Log.d("GET RATE OK", result.toString());

                                requestCommissionCallback.onSuccess(result);
                            }
                        }
                    });

                } catch (ApiException apiException) {
                    Log.d("GET RATE FAILURE", apiException.getMessage());
                    requestCommissionCallback.onFailure(apiException.getMessage());
                }
            }
        }
    }

    @Override
    public void initiateTransfer(final Transfer transfer, final TransferContract.ProcessTransferCallback processTransferCallback) {

        // TODO: do I need to get new token?

        if (session.getAccessToken() != null) {

            Transaction transaction = new Transaction();
            transaction.amountCentis(transfer.getTransaction().getAmountCentis())
                    .currency(transfer.getTransaction().getCurrency());

            InitiateTransferRequest initiateTransferRequest = new InitiateTransferRequest();
            initiateTransferRequest.consumer(consumer)
                    .session(session)
                    .transaction(transaction);

            try {
                merchantApi.transferInitiateTransferPostAsync(initiateTransferRequest, new DengisendApiCallback<InitiateTransferResponse>() {
                    @Override
                    public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                        Log.d("INIT TRANSFER FAILURE", e.getMessage());

                        processTransferCallback.onInitiateTransferFailure(e.getMessage());
                    }

                    @Override
                    public void onSuccess(InitiateTransferResponse result, int statusCode, Map<String, List<String>> responseHeaders) {

                        if (result.getError() != null) {
                            Log.d("INIT TRANSFER ERROR", result.getError().toString());

                            processTransferCallback.onInitiateTransferFailure(result.getError().toString());
                        } else {
                            Log.d("INIT TRANSFER OK", result.toString());

                            processTransferCallback.onInitiateTransferSuccess();

                            transfer.getTransaction().setEndpointId(result.getEndpointId());
                            transfer.getTransaction().setInvoiceId(result.getInvoiceId());

                            session.setNonce(result.getSession().getNonce());
                            session.setSignature(result.getSession().getSignature());

                            performTransfer(transfer, processTransferCallback);
                        }
                    }
                });
            } catch (ApiException apiException) {
                Log.d("INIT TRANSFER EXCEPTION", apiException.getMessage());

                processTransferCallback.onInitiateTransferFailure(apiException.getMessage());
            }
        }
    }

    @Override
    public void performTransfer(final Transfer transfer, final TransferContract.ProcessTransferCallback processTransferCallback) {

        Log.d("PERFORM TRANSFER", transfer.toString());

        String endpointId = transfer.getTransaction().getEndpointId();
        String invoiceId = transfer.getTransaction().getInvoiceId();

        final Receipt receipt = transfer.getReceipt();

        receipt.setInvoiceId(transfer.getTransaction().getInvoiceId());

        if (transfer.getSourceCard().getCard() != null) {
            receipt.setSourceCard(transfer.getSourceCard().getCard().getNumber());
        }

        if (transfer.getSourceCard().getReference() != null) {
            receipt.setSourceCardId(transfer.getSourceCard().getReference().getClientCardId());
        }

        if (transfer.getDestCard().getCard() != null) {
            receipt.setDestCard(transfer.getDestCard().getCard().getNumber());
        }

        if (transfer.getDestCard().getReference() != null) {
            receipt.setDestCardId(transfer.getDestCard().getReference().getClientCardId());
        }

        SourceCard sourceCard = transfer.getSourceCard();
        DestinationCard destinationCard = transfer.getDestCard();

        if(sourceCard.getCard().getSecurityCode() == null){
            sourceCard.setCard(null);
            destinationCard.setCard(null);
        }

        PerformTransferRequest performTransferRequestData = new PerformTransferRequest();
        performTransferRequestData.consumer(consumer)
                .session(session)
                .transaction(transfer.getTransaction())
                .sourceOfFunds(transfer.getSourceCard())
                .destinationOfFunds(transfer.getDestCard());

        DengisendApiCallback<PerformTransferResponse> performTransferResponseApiCallback = new DengisendApiCallback<PerformTransferResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                Log.d("PERFORM TRANSFER ERROR", e.getMessage());

                processTransferCallback.onPerformTransferFailure(e.getMessage());
            }

            @Override
            public void onSuccess(PerformTransferResponse result, int statusCode, Map<String, List<String>> responseHeaders) {

                if (result.getError() != null) {
                    Log.d("PERFORM TRANSFER ERROR", result.getError().toString());

                    processTransferCallback.onPerformTransferFailure(result.getError().toString());
                } else {
                    Log.d("PERFORM TRANSFER OK", result.toString());

                    processTransferCallback.onPerformTransferSuccess();

                    session.setToken(result.getSession().getToken());

                    receipt.setStatus(TransferStatus.UNKNOWN.toString());

                    // Going to check the transfer status
                    checkTransferStatus(transfer, processTransferCallback);
                }
            }
        };

        try {
            // PERFORM TRANSFER
            paynetEasyApi.transferEndpointIdInvoiceIdPostAsync(endpointId, invoiceId,
                    performTransferRequestData, performTransferResponseApiCallback);
        } catch (ApiException apiException) {
            Log.d("PERFORM TRANSFER ERROR", apiException.getMessage());

            processTransferCallback.onPerformTransferFailure(apiException.getMessage());
        }
    }

    @Override
    public void checkTransferStatus(final Transfer transfer, final TransferContract.ProcessTransferCallback processTransferCallback) {

        String endpointId = transfer.getTransaction().getEndpointId();
        String invoiceId = transfer.getTransaction().getInvoiceId();

        TransferStatusRequest checkTransferRequest = new TransferStatusRequest();
        checkTransferRequest.setSession(session);

        DengisendApiCallback<TransferStatusResponse> callback = new DengisendApiCallback<TransferStatusResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                Log.d("TRANSFER STATUS ERROR", e.getMessage());

                processTransferCallback.onCheckTransferStatusFailure(e.getMessage());
            }

            @Override
            public void onSuccess(TransferStatusResponse result, int statusCode, Map<String, List<String>> responseHeaders) {

                if (result.getError() != null) {
                    Log.d("TRANSFER STATUS ERROR", result.getError().toString());

                    processTransferCallback.onCheckTransferStatusFailure(result.getError().toString());

                    transfer.getReceipt().setStatus(TransferStatus.ERROR.toString());

                    // TODO: what happened with the request? what is the actual transfer's status?

                } else {
                    Log.d("TRANSFER STATUS OK", result.toString());

                    processTransferCallback.onCheckTransferStatusSuccess();

                    boolean needRepeat = false;

                    TransferStatusResponse.StateEnum state = result.getState();

                    // PROCESSING
                    if (state == TransferStatusResponse.StateEnum.PROCESSING) {
                        needRepeat = true;

                        processTransferCallback.onTransferProcessing();
                    }
                    // APPROVED
                    else if (state == TransferStatusResponse.StateEnum.APPROVED) {

                        // Fill receipt with data and set status
                        parseReceipt(transfer.getReceipt(), result);

                        processTransferCallback.onTransferApproved(transfer.getReceipt());
                    }
                    // REDIRECT
                    else if (state == TransferStatusResponse.StateEnum.REDIRECT_REQUEST) {

                        processTransferCallback.onTransferRedirect(result.getRedirectUrl());

                        needRepeat = true;
                    }
                    // DECLINED
                    else {

                        // Fill receipt with data and set status
                        parseReceipt(transfer.getReceipt(), result);

                        processTransferCallback.onTransferDeclined(transfer.getReceipt());
                    }

                    // repeat
                    if (needRepeat) {
                        try {
                            Log.d("TRANSFER STATUS OK", "going to repeat");
                            Thread.sleep(_updateInterval);
                            checkTransferStatus(transfer, processTransferCallback);
                        } catch (InterruptedException e) {
                            Log.d("TRANSFER STATUS Error", "repeat request failed");
                            processTransferCallback.onCheckTransferStatusFailure(e.getMessage());
                        }
                    }
                }
            }

            private void parseReceipt(Receipt receipt, TransferStatusResponse result) {
                receipt.setOrderId(result.getOrderId());
                receipt.setDate(result.getTransaction().getTransactionCreatedDate());
                receipt.setAmountCentis(result.getTransaction().getAmountCentis().intValue());
                receipt.setCommissionCentis(result.getTransaction().getCommissionCentis().intValue());
                receipt.setCurrency(result.getTransaction().getCurrency());

                if (result.getState() == TransferStatusResponse.StateEnum.APPROVED){
                    receipt.setStatus(TransferStatus.APPROVED.toString());
                }

                if (result.getState() == TransferStatusResponse.StateEnum.DECLINED){
                    receipt.setStatus(TransferStatus.DECLINED.toString());
                }
            }
        };

        try {
            // TRANSFER STATUS
            paynetEasyApi.transferEndpointIdInvoiceIdStatusPostAsync(endpointId, invoiceId, checkTransferRequest, callback);
        } catch (ApiException apiException) {
            Log.d("TRANSFER STATUS Error", apiException.getMessage());

            processTransferCallback.onCheckTransferStatusFailure(apiException.getMessage());
        }
    }

    @Override
    public void getCardsIdsForInvoiceId(final String invoiceId, final TransferContract.RequestCardsIdsCallback cardsIdsCallback){

        clearSession();

        // get new token
        requestAccessToken(new TransferContract.RequestAccessTokenCallback() {
            @Override
            public void onFailure(String errorMessage) {
                cardsIdsCallback.onFailure(errorMessage);
            }

            @Override
            public void onSuccess(AccessTokenResponse result) {

                // get rates
                requestCommission(new TransferContract.RequestCommissionCallback() {
                    @Override
                    public void onFailure(String errorMessage) {
                        cardsIdsCallback.onFailure(errorMessage);
                    }

                    @Override
                    public void onSuccess(RatesResponse result) {

                        CardsIdsRequest cardsIdsRequest = new CardsIdsRequest();
                        cardsIdsRequest.session(session).consumer(consumer);

                        DengisendApiCallback<CardsIdsResponse> cardsIdsResponseApiCallback = new DengisendApiCallback<CardsIdsResponse>() {
                            @Override
                            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                                cardsIdsCallback.onFailure(e.getMessage());
                            }

                            @Override
                            public void onSuccess(CardsIdsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                                cardsIdsCallback.onSuccess(result);
                            }
                        };

                        try {
                            // FETCH CARDs IDs
                            merchantApi.cardrefsInvoiceIdGetClientIdsPostAsync(invoiceId, cardsIdsRequest, cardsIdsResponseApiCallback);
                        } catch (ApiException apiException) {
                            cardsIdsCallback.onFailure(apiException.getMessage());
                        }
                    }
                });
            }
        });
    }
}
