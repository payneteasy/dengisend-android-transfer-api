# Android

## Getting Started

### Models

Implementation examples are in a "models" folder .

### Create and setup MerchantAPI instance

```java

// MERCHANT API
ApiClient merchantApiClient = new ApiClient();
merchantApiClient.setBasePath(Config.MERCHANT_BASE_ADDRESS);
merchantApi = new DefaultApi(merchantApiClient);

```

### Create and setup PaynetEasyTransferAPI instance

```java

// PNE API
ApiClient paynetEasyApiClient = new ApiClient();
paynetEasyApiClient.setBasePath(Config.PAYNET_BASE_ADDRESS);
paynetEasyApi = new DefaultApi(paynetEasyApiClient);

```

### Get access token

```java
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

merchantApi.authBankIdRequestAccessTokenGetAsync(Config.DENGISEND_IDENTIFIER, new DengisendApiCallback<AccessTokenResponse>() {
    @Override
    public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
        Log.d("ACCESS TOKEN FAILURE", e.getMessage());
        
        requestAccessTokenCallback.onFailure(e.getMessage());
    }
    
    @Override
    public void onSuccess(AccessTokenResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
        Log.d("ACCESS TOKEN OK", result.getSession().getAccessToken());
        
        requestAccessTokenCallback.onSuccess(result);
    }
});
```

### Get transfer rate

```java
merchantApi.authBankIdRequestAccessTokenGetAsync(Config.DENGISEND_IDENTIFIER, new DengisendApiCallback<AccessTokenResponse>() {
    @Override
    public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
        Log.d("ACCESS TOKEN FAILURE", e.getMessage());
        
        requestAccessTokenCallback.onFailure(e.getMessage());
    }
    
    @Override
    public void onSuccess(AccessTokenResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
        Log.d("ACCESS TOKEN OK", result.getSession().getAccessToken());
        
        requestAccessTokenCallback.onSuccess(result);
    }
});
```

### Initiate Transfer

```java
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
```

### Perform Transfer

```java
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
```

### Check Transfer Status

```java
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
```

### Get cards Ids

```java
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
```

### Example of TransferContract

```java
public interface TransferContract {

    interface Service {

        void clearSession();

        void requestCommission(RequestCommissionCallback requestCommissionCallback);

        void initiateTransfer(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void performTransfer(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void checkTransferStatus(Transfer transfer, TransferContract.ProcessTransferCallback processTransferCallback);

        void getCardsIdsForInvoiceId(final String invoiceId, TransferContract.RequestCardsIdsCallback cardsIdsCallback);
    }

    interface RequestCommissionCallback {

        void onFailure(String errorMessage);

        void onSuccess(RatesResponse result);
    }

    interface RequestAccessTokenCallback {

        void onFailure(String errorMessage);

        void onSuccess(AccessTokenResponse result);
    }

    interface ProcessTransferCallback {

        void onInitiateTransferFailure(String errorMessage);

        void onInitiateTransferSuccess();

        void onPerformTransferFailure(String errorMessage);

        void onPerformTransferSuccess();

        void onCheckTransferStatusFailure(String errorMessage);

        void onCheckTransferStatusSuccess();

        void onTransferProcessing();

        void onTransferRedirect(String redirectUrl);

        void onTransferApproved(Receipt receipt);

        void onTransferDeclined(Receipt receipt);
    }

    interface RequestCardsIdsCallback {

        void onFailure(String errorMessage);

        void onSuccess(CardsIdsResponse result);
    }
}
```

### Example of DengisendApiCallback

```java
public abstract class DengisendApiCallback<T> implements ApiCallback<T> {
    @Override
    public abstract void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders);

    @Override
    public abstract void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders);

    @Override
    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) { }

    @Override
    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) { }
}
```

## Documentation for API Endpoints

All URIs are relative to *https://localhost*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**authBankIdRequestAccessTokenGet**](docs/DefaultApi.md#authBankIdRequestAccessTokenGet) | **GET** /auth/{bank_id}/request-access-token | Generate access token for transfer operation
*DefaultApi* | [**cardrefsInvoiceIdGetClientIdsPost**](docs/DefaultApi.md#cardrefsInvoiceIdGetClientIdsPost) | **POST** /cardrefs/{invoiceId}/get-client-ids | Fetch source and destination cards ids
*DefaultApi* | [**configGetRatePost**](docs/DefaultApi.md#configGetRatePost) | **POST** /config/get-rate | Get transfer rates and limits configuration
*DefaultApi* | [**supportSendMessagePost**](docs/DefaultApi.md#supportSendMessagePost) | **POST** /support/send-message | send feedback to support
*DefaultApi* | [**transferEndpointIdInvoiceIdPost**](docs/DefaultApi.md#transferEndpointIdInvoiceIdPost) | **POST** /transfer/{endpointId}/{invoiceId} | Perform transfer request
*DefaultApi* | [**transferEndpointIdInvoiceIdStatusPost**](docs/DefaultApi.md#transferEndpointIdInvoiceIdStatusPost) | **POST** /transfer/{endpointId}/{invoiceId}/status | Get funds transfer status
*DefaultApi* | [**transferInitiateTransferPost**](docs/DefaultApi.md#transferInitiateTransferPost) | **POST** /transfer/initiate-transfer | Initiate transfer request


## Documentation for Models

 - [AccessTokenResponse](docs/AccessTokenResponse.md)
 - [CardsIdsRequest](docs/CardsIdsRequest.md)
 - [CardsIdsResponse](docs/CardsIdsResponse.md)
 - [Consumer](docs/Consumer.md)
 - [ConsumerDevice](docs/ConsumerDevice.md)
 - [DestinationOfFunds](docs/DestinationOfFunds.md)
 - [DestinationOfFundsCard](docs/DestinationOfFundsCard.md)
 - [DestinationOfFundsReference](docs/DestinationOfFundsReference.md)
 - [Error](docs/Error.md)
 - [ErrorResponse](docs/ErrorResponse.md)
 - [InitiateTransferRequest](docs/InitiateTransferRequest.md)
 - [InitiateTransferRequestLocation](docs/InitiateTransferRequestLocation.md)
 - [InitiateTransferResponse](docs/InitiateTransferResponse.md)
 - [InitiateTransferResponseRates](docs/InitiateTransferResponseRates.md)
 - [PerformTransferRequest](docs/PerformTransferRequest.md)
 - [PerformTransferResponse](docs/PerformTransferResponse.md)
 - [RatesRequest](docs/RatesRequest.md)
 - [RatesResponse](docs/RatesResponse.md)
 - [RatesResponseError](docs/RatesResponseError.md)
 - [SendFeedbackRequest](docs/SendFeedbackRequest.md)
 - [SendFeedbackResponse](docs/SendFeedbackResponse.md)
 - [Session](docs/Session.md)
 - [SourceOfFunds](docs/SourceOfFunds.md)
 - [SourceOfFundsCard](docs/SourceOfFundsCard.md)
 - [SourceOfFundsCardExpiry](docs/SourceOfFundsCardExpiry.md)
 - [SourceOfFundsCardHolder](docs/SourceOfFundsCardHolder.md)
 - [SourceOfFundsReference](docs/SourceOfFundsReference.md)
 - [Transaction](docs/Transaction.md)
 - [TransferStatusRequest](docs/TransferStatusRequest.md)
 - [TransferStatusResponse](docs/TransferStatusResponse.md)


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.


