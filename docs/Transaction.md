# Transaction

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**TypeEnum**](#TypeEnum) | Transaction type |  [optional]
**fromBin** | **Integer** | The Source card BIN |  [optional]
**toBin** | **Integer** | The Destination card BIN |  [optional]
**endpointId** | **String** | Entry point identifier for transfer transaction |  [optional]
**invoiceId** | **String** | Transfer transaction identifier |  [optional]
**amountCentis** | **Long** | Transaction amount, 1.00 EUR &#x3D; 100 centis |  [optional]
**commissionCentis** | **Long** | Amount of commission held in centis |  [optional]
**currency** | **String** | Transaction currency, upper case letters (ISO 4217 alpha code) |  [optional]
**orderCreatedDate** | **String** | Order creation date in payment processing system |  [optional]
**transactionCreatedDate** | **String** | Transaction creation date in payment processing system |  [optional]

<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----
INTERNAL | &quot;internal&quot;
INTERNATIONAL | &quot;international&quot;
