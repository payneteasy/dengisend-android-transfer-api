# TransferStatusResponse

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**bankOrderId** | **String** | Required if state is APPROVED or DECLINED |  [optional]
**orderId** | **String** | Order identifier in payment processing system |  [optional]
**invoiceId** | **String** | Transfer transaction identifier |  [optional]
**redirectUrl** | **String** | Required if state is REDIRECT_REQUEST |  [optional]
**session** | [**Session**](Session.md) |  |  [optional]
**state** | [**StateEnum**](#StateEnum) | The state of the transfer |  [optional]
**transaction** | [**Transaction**](Transaction.md) |  |  [optional]
**error** | [**Error**](Error.md) |  |  [optional]
**errorCode** | **Integer** | Decline reason code, may present if state is DECLINED |  [optional]
**errorMessage** | **String** | Decline reason message, may present if state is DECLINED |  [optional]
**secure3DAuthStatus** | [**Secure3DAuthStatusEnum**](#Secure3DAuthStatusEnum) | 3D authentication status |  [optional]
**randomSumAuthStatus** | [**RandomSumAuthStatusEnum**](#RandomSumAuthStatusEnum) | Random sum authentication status |  [optional]

<a name="StateEnum"></a>
## Enum: StateEnum
Name | Value
---- | -----
PROCESSING | &quot;PROCESSING&quot;
REDIRECT_REQUEST | &quot;REDIRECT_REQUEST&quot;
APPROVED | &quot;APPROVED&quot;
DECLINED | &quot;DECLINED&quot;

<a name="Secure3DAuthStatusEnum"></a>
## Enum: Secure3DAuthStatusEnum
Name | Value
---- | -----
AUTHENTICATED | &quot;AUTHENTICATED&quot;
NOT_AUTHENTICATED | &quot;NOT_AUTHENTICATED&quot;
UNSUPPORTED | &quot;UNSUPPORTED&quot;
DECLINED | &quot;DECLINED&quot;

<a name="RandomSumAuthStatusEnum"></a>
## Enum: RandomSumAuthStatusEnum
Name | Value
---- | -----
AUTHENTICATED | &quot;AUTHENTICATED&quot;
NOT_AUTHENTICATED | &quot;NOT_AUTHENTICATED&quot;
