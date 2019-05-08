# DefaultApi

All URIs are relative to */*

Method | HTTP request | Description
------------- | ------------- | -------------
[**authBankIdRequestAccessTokenGet**](DefaultApi.md#authBankIdRequestAccessTokenGet) | **GET** auth/{bank_id}/request-access-token | Generate access token for transfer operation
[**cardrefsInvoiceIdGetClientIdsPost**](DefaultApi.md#cardrefsInvoiceIdGetClientIdsPost) | **POST** cardrefs/{invoiceId}/get-client-ids | Fetch source and destination cards ids
[**configGetCommissionPost**](DefaultApi.md#configGetCommissionPost) | **POST** config/get-commission | Get transaction commission
[**configGetRatePost**](DefaultApi.md#configGetRatePost) | **POST** config/get-rate | Get transfer rates and limits configuration
[**contactsStatusPost**](DefaultApi.md#contactsStatusPost) | **POST** contacts/status | send feedback to support
[**supportSendMessagePost**](DefaultApi.md#supportSendMessagePost) | **POST** support/send-message | send feedback to support
[**transferEndpointIdInvoiceIdPost**](DefaultApi.md#transferEndpointIdInvoiceIdPost) | **POST** transfer/{endpointId}/{invoiceId} | Perform transfer request
[**transferEndpointIdInvoiceIdStatusPost**](DefaultApi.md#transferEndpointIdInvoiceIdStatusPost) | **POST** transfer/{endpointId}/{invoiceId}/status | Get funds transfer status
[**transferInitiateTransferPost**](DefaultApi.md#transferInitiateTransferPost) | **POST** transfer/initiate-transfer | Initiate transfer request

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
> CardsIdsResponse cardrefsInvoiceIdGetClientIdsPost(body, invoiceId)

Fetch source and destination cards ids

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
CardsIdsRequest body = new CardsIdsRequest(); // CardsIdsRequest | transfer data
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
try {
    CardsIdsResponse result = apiInstance.cardrefsInvoiceIdGetClientIdsPost(body, invoiceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#cardrefsInvoiceIdGetClientIdsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CardsIdsRequest**](CardsIdsRequest.md)| transfer data |
 **invoiceId** | **String**| Transfer transaction identifier |

### Return type

[**CardsIdsResponse**](CardsIdsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="configGetCommissionPost"></a>
# **configGetCommissionPost**
> CommissionResponse configGetCommissionPost(body)

Get transaction commission

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
CommissionRequest body = new CommissionRequest(); // CommissionRequest | Session info
try {
    CommissionResponse result = apiInstance.configGetCommissionPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#configGetCommissionPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CommissionRequest**](CommissionRequest.md)| Session info |

### Return type

[**CommissionResponse**](CommissionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="configGetRatePost"></a>
# **configGetRatePost**
> RatesResponse configGetRatePost(body)

Get transfer rates and limits configuration

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
RatesRequest body = new RatesRequest(); // RatesRequest | Session info
try {
    RatesResponse result = apiInstance.configGetRatePost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#configGetRatePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RatesRequest**](RatesRequest.md)| Session info |

### Return type

[**RatesResponse**](RatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="contactsStatusPost"></a>
# **contactsStatusPost**
> ContactsStatusResponse contactsStatusPost(body)

send feedback to support

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
ContactsStatusRequest body = new ContactsStatusRequest(); // ContactsStatusRequest | Account data to check
try {
    ContactsStatusResponse result = apiInstance.contactsStatusPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#contactsStatusPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ContactsStatusRequest**](ContactsStatusRequest.md)| Account data to check |

### Return type

[**ContactsStatusResponse**](ContactsStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="supportSendMessagePost"></a>
# **supportSendMessagePost**
> SendFeedbackResponse supportSendMessagePost(body)

send feedback to support

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
SendFeedbackRequest body = new SendFeedbackRequest(); // SendFeedbackRequest | Transfer data to check
try {
    SendFeedbackResponse result = apiInstance.supportSendMessagePost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#supportSendMessagePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SendFeedbackRequest**](SendFeedbackRequest.md)| Transfer data to check |

### Return type

[**SendFeedbackResponse**](SendFeedbackResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferEndpointIdInvoiceIdPost"></a>
# **transferEndpointIdInvoiceIdPost**
> PerformTransferResponse transferEndpointIdInvoiceIdPost(body, endpointId, invoiceId)

Perform transfer request

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
PerformTransferRequest body = new PerformTransferRequest(); // PerformTransferRequest | transfer data
String endpointId = "endpointId_example"; // String | Entry point identifier for transfer transaction
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
try {
    PerformTransferResponse result = apiInstance.transferEndpointIdInvoiceIdPost(body, endpointId, invoiceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferEndpointIdInvoiceIdPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**PerformTransferRequest**](PerformTransferRequest.md)| transfer data |
 **endpointId** | **String**| Entry point identifier for transfer transaction |
 **invoiceId** | **String**| Transfer transaction identifier |

### Return type

[**PerformTransferResponse**](PerformTransferResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferEndpointIdInvoiceIdStatusPost"></a>
# **transferEndpointIdInvoiceIdStatusPost**
> TransferStatusResponse transferEndpointIdInvoiceIdStatusPost(body, endpointId, invoiceId)

Get funds transfer status

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
TransferStatusRequest body = new TransferStatusRequest(); // TransferStatusRequest | Transfer data to check
String endpointId = "endpointId_example"; // String | Entry point identifier for transfer transaction
String invoiceId = "invoiceId_example"; // String | Transfer transaction identifier
try {
    TransferStatusResponse result = apiInstance.transferEndpointIdInvoiceIdStatusPost(body, endpointId, invoiceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferEndpointIdInvoiceIdStatusPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**TransferStatusRequest**](TransferStatusRequest.md)| Transfer data to check |
 **endpointId** | **String**| Entry point identifier for transfer transaction |
 **invoiceId** | **String**| Transfer transaction identifier |

### Return type

[**TransferStatusResponse**](TransferStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="transferInitiateTransferPost"></a>
# **transferInitiateTransferPost**
> InitiateTransferResponse transferInitiateTransferPost(body)

Initiate transfer request

### Example
```java
// Import classes:
//import com.payneteasy.android.ApiException;
//import com.payneteasy.android.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
InitiateTransferRequest body = new InitiateTransferRequest(); // InitiateTransferRequest | Device info, location data, session info, amount
try {
    InitiateTransferResponse result = apiInstance.transferInitiateTransferPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#transferInitiateTransferPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InitiateTransferRequest**](InitiateTransferRequest.md)| Device info, location data, session info, amount |

### Return type

[**InitiateTransferResponse**](InitiateTransferResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

