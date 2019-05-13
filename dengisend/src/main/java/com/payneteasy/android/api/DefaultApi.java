package com.payneteasy.android.api;

import com.payneteasy.android.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.payneteasy.android.model.AccessTokenResponse;
import com.payneteasy.android.model.CardsIdsRequest;
import com.payneteasy.android.model.CardsIdsResponse;
import com.payneteasy.android.model.CommissionRequest;
import com.payneteasy.android.model.CommissionResponse;
import com.payneteasy.android.model.ContactsStatusRequest;
import com.payneteasy.android.model.ContactsStatusResponse;
import com.payneteasy.android.model.ErrorResponse;
import com.payneteasy.android.model.InitiateTransferRequest;
import com.payneteasy.android.model.InitiateTransferResponse;
import com.payneteasy.android.model.PerformTransferRequest;
import com.payneteasy.android.model.PerformTransferResponse;
import com.payneteasy.android.model.RatesRequest;
import com.payneteasy.android.model.RatesResponse;
import com.payneteasy.android.model.SendFeedbackRequest;
import com.payneteasy.android.model.SendFeedbackResponse;
import com.payneteasy.android.model.TransferStatusRequest;
import com.payneteasy.android.model.TransferStatusResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DefaultApi {
  /**
   * Generate access token for transfer operation
   * 
   * @param bankId Bank identifier (required)
   * @return Call&lt;AccessTokenResponse&gt;
   */
  @GET("auth/{bank_id}/request-access-token")
  Call<AccessTokenResponse> authBankIdRequestAccessTokenGet(
            @retrofit2.http.Path("bank_id") String bankId            
  );

  /**
   * Fetch source and destination cards ids
   * 
   * @param body transfer data (required)
   * @param invoiceId Transfer transaction identifier (required)
   * @return Call&lt;CardsIdsResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("cardrefs/{invoiceId}/get-client-ids")
  Call<CardsIdsResponse> cardrefsInvoiceIdGetClientIdsPost(
                    @retrofit2.http.Body CardsIdsRequest body    ,         @retrofit2.http.Path("invoiceId") String invoiceId            
  );

  /**
   * Get transaction commission
   * 
   * @param body Session info (required)
   * @return Call&lt;CommissionResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("config/get-commission")
  Call<CommissionResponse> configGetCommissionPost(
                    @retrofit2.http.Body CommissionRequest body    
  );

  /**
   * Get transfer rates and limits configuration
   * 
   * @param body Session info (required)
   * @return Call&lt;RatesResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("config/get-rate")
  Call<RatesResponse> configGetRatePost(
                    @retrofit2.http.Body RatesRequest body    
  );

  /**
   * send feedback to support
   * 
   * @param body Account data to check (required)
   * @return Call&lt;ContactsStatusResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("contacts/status")
  Call<ContactsStatusResponse> contactsStatusPost(
                    @retrofit2.http.Body ContactsStatusRequest body    
  );

  /**
   * send feedback to support
   * 
   * @param body Transfer data to check (required)
   * @return Call&lt;SendFeedbackResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("support/send-message")
  Call<SendFeedbackResponse> supportSendMessagePost(
                    @retrofit2.http.Body SendFeedbackRequest body    
  );

  /**
   * Perform transfer request
   * 
   * @param body transfer data (required)
   * @param endpointId Entry point identifier for transfer transaction (required)
   * @param invoiceId Transfer transaction identifier (required)
   * @return Call&lt;PerformTransferResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("transfer/{endpointId}/{invoiceId}")
  Call<PerformTransferResponse> transferEndpointIdInvoiceIdPost(
                    @retrofit2.http.Body PerformTransferRequest body    ,         @retrofit2.http.Path("endpointId") String endpointId            ,         @retrofit2.http.Path("invoiceId") String invoiceId            
  );

  /**
   * Get funds transfer status
   * 
   * @param body Transfer data to check (required)
   * @param endpointId Entry point identifier for transfer transaction (required)
   * @param invoiceId Transfer transaction identifier (required)
   * @return Call&lt;TransferStatusResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("transfer/{endpointId}/{invoiceId}/status")
  Call<TransferStatusResponse> transferEndpointIdInvoiceIdStatusPost(
                    @retrofit2.http.Body TransferStatusRequest body    ,         @retrofit2.http.Path("endpointId") String endpointId            ,         @retrofit2.http.Path("invoiceId") String invoiceId            
  );

  /**
   * Initiate transfer request
   * 
   * @param body Device info, location data, session info, amount (required)
   * @return Call&lt;InitiateTransferResponse&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("transfer/initiate-transfer")
  Call<InitiateTransferResponse> transferInitiateTransferPost(
                    @retrofit2.http.Body InitiateTransferRequest body    
  );

}
