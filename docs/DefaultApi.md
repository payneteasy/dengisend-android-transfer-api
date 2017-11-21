# DefaultApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**authBankIdRequestAccessTokenGet**](DefaultApi.md#authBankIdRequestAccessTokenGet) | **GET** /auth/{bank_id}/request-access-token | Generate access token for transfer operation
[**cardrefsInvoiceIdGetClientIdsPost**](DefaultApi.md#cardrefsInvoiceIdGetClientIdsPost) | **POST** /cardrefs/{invoiceId}/get-client-ids | Fetch source and destination cards ids
[**configGetRatePost**](DefaultApi.md#configGetRatePost) | **POST** /config/get-rate | Get transfer rates and limits configuration
[**supportSendMessagePost**](DefaultApi.md#supportSendMessagePost) | **POST** /support/send-message | send feedback to support
[**transferEndpointIdInvoiceIdPost**](DefaultApi.md#transferEndpointIdInvoiceIdPost) | **POST** /transfer/{endpointId}/{invoiceId} | Perform transfer request
[**transferEndpointIdInvoiceIdStatusPost**](DefaultApi.md#transferEndpointIdInvoiceIdStatusPost) | **POST** /transfer/{endpointId}/{invoiceId}/status | Get funds transfer status
[**transferInitiateTransferPost**](DefaultApi.md#transferInitiateTransferPost) | **POST** /transfer/initiate-transfer | Initiate transfer request


<a name="authBankIdRequestAccessTokenGet"></a>
# **authBankIdRequestAccessTokenGet**
> AccessTokenResponse authBankIdRequestAccessTokenGet(bankId)

Generate access token for transfer operation

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String bankId = "bankId_example"; // String | Bank identifier
try {
    AccessTokenResponse result = apiInstance.authBankIdRequestAccessTokenGet(bankId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#authBankIdRequestAccessTokenGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **bankId** | **String**| Bank identifier |

### Return type

[**AccessTokenResponse**](AccessTokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="cardrefsInvoiceIdGetClientIdsPost"></a>
# **cardrefsInvoiceIdGetClientIdsPost**
> CardsIdsResponse cardrefsInvoiceIdGetClientIdsPost(invoiceId, performTransferRequestData)

Fetch source and destination cards ids

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
CardsIdsRequest performTransferRequestData = new CardsIdsRequest(); // CardsIdsRequest | transfer data
try {
    CardsIdsResponse result = apiInstance.cardrefsInvoiceIdGetClientIdsPost(invoiceId, performTransferRequestData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#cardrefsInvoiceIdGetClientIdsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **invoiceId** | **String**| Transfer transaction identifier |
 **performTransferRequestData** | [**CardsIdsRequest**](CardsIdsRequest.md)| transfer data |

### Return type

[**CardsIdsResponse**](CardsIdsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="configGetRatePost"></a>
# **configGetRatePost**
> RatesResponse configGetRatePost(sessionData)

Get transfer rates and limits configuration

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
RatesRequest sessionData = new RatesRequest(); // RatesRequest | Session info
try {
    RatesResponse result = apiInstance.configGetRatePost(sessionData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#configGetRatePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sessionData** | [**RatesRequest**](RatesRequest.md)| Session info |

### Return type

[**RatesResponse**](RatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="supportSendMessagePost"></a>
# **supportSendMessagePost**
> SendFeedbackResponse supportSendMessagePost(sendFeedbackRequest)

send feedback to support

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
SendFeedbackRequest sendFeedbackRequest = new SendFeedbackRequest(); // SendFeedbackRequest | Transfer data to check
try {
    SendFeedbackResponse result = apiInstance.supportSendMessagePost(sendFeedbackRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#supportSendMessagePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sendFeedbackRequest** | [**SendFeedbackRequest**](SendFeedbackRequest.md)| Transfer data to check |

### Return type

[**SendFeedbackResponse**](SendFeedbackResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferEndpointIdInvoiceIdPost"></a>
# **transferEndpointIdInvoiceIdPost**
> PerformTransferResponse transferEndpointIdInvoiceIdPost(endpointId, invoiceId, performTransferRequestData)

Perform transfer request

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String endpointId = "endpointId_example"; // String | Entry point identifier for transfer transaction
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
PerformTransferRequest performTransferRequestData = new PerformTransferRequest(); // PerformTransferRequest | transfer data
try {
    PerformTransferResponse result = apiInstance.transferEndpointIdInvoiceIdPost(endpointId, invoiceId, performTransferRequestData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferEndpointIdInvoiceIdPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **endpointId** | **String**| Entry point identifier for transfer transaction |
 **invoiceId** | **String**| Transfer transaction identifier |
 **performTransferRequestData** | [**PerformTransferRequest**](PerformTransferRequest.md)| transfer data |

### Return type

[**PerformTransferResponse**](PerformTransferResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferEndpointIdInvoiceIdStatusPost"></a>
# **transferEndpointIdInvoiceIdStatusPost**
> TransferStatusResponse transferEndpointIdInvoiceIdStatusPost(endpointId, invoiceId, transferStatusRequest)

Get funds transfer status

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String endpointId = "endpointId_example"; // String | Entry point identifier for transfer transaction
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
TransferStatusRequest transferStatusRequest = new TransferStatusRequest(); // TransferStatusRequest | Transfer data to check
try {
    TransferStatusResponse result = apiInstance.transferEndpointIdInvoiceIdStatusPost(endpointId, invoiceId, transferStatusRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferEndpointIdInvoiceIdStatusPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **endpointId** | **String**| Entry point identifier for transfer transaction |
 **invoiceId** | **String**| Transfer transaction identifier |
 **transferStatusRequest** | [**TransferStatusRequest**](TransferStatusRequest.md)| Transfer data to check |

### Return type

[**TransferStatusResponse**](TransferStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferInitiateTransferPost"></a>
# **transferInitiateTransferPost**
> InitiateTransferResponse transferInitiateTransferPost(initiateTransferData)

Initiate transfer request

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
InitiateTransferRequest initiateTransferData = new InitiateTransferRequest(); // InitiateTransferRequest | Device info, location data, session info, amount
try {
    InitiateTransferResponse result = apiInstance.transferInitiateTransferPost(initiateTransferData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferInitiateTransferPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **initiateTransferData** | [**InitiateTransferRequest**](InitiateTransferRequest.md)| Device info, location data, session info, amount |

### Return type

[**InitiateTransferResponse**](InitiateTransferResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

