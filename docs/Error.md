# Error

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**cause** | [**CauseEnum**](#CauseEnum) | The cause of the error |  [optional]
**code** | **String** | The unique error code |  [optional]
**message** | **String** | the description of the error |  [optional]

<a name="CauseEnum"></a>
## Enum: CauseEnum
Name | Value
---- | -----
FILTERED | &quot;FILTERED&quot;
DECLINED | &quot;DECLINED&quot;
INVALID_CONFIGURATION | &quot;INVALID_CONFIGURATION&quot;
INVALID_REQUEST | &quot;INVALID_REQUEST&quot;
INTERNAL_ERROR | &quot;INTERNAL_ERROR&quot;
PROCESSING_FAILED | &quot;PROCESSING_FAILED&quot;
SERVER_FAILED | &quot;SERVER_FAILED&quot;
SERVER_UNAVAILABLE | &quot;SERVER_UNAVAILABLE&quot;
ORDER_NOT_FOUND | &quot;ORDER_NOT_FOUND&quot;
