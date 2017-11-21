# android

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.payneteasy</groupId>
    <artifactId>android</artifactId>
    <version>0.1</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.payneteasy:android:0.1"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/android-0.1.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.payneteasy.android.*;
import com.payneteasy.android.auth.*;
import com.payneteasy.android.model.*;
import com.payneteasy.android.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        String bankId = "bankId_example"; // String | Bank identifier
        try {
            AccessTokenResponse result = apiInstance.authBankIdRequestAccessTokenGet(bankId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#authBankIdRequestAccessTokenGet");
            e.printStackTrace();
        }
    }
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


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



