/*
 * Payneteasy Android Transfer DAPI
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.payneteasy.android.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Session
 */

public class Session {
  @SerializedName("accessToken")
  private String accessToken = null;

  @SerializedName("nonce")
  private String nonce = null;

  @SerializedName("signature")
  private String signature = null;

  @SerializedName("token")
  private String token = null;

  public Session accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

   /**
   * The consumer’s access token key
   * @return accessToken
  **/
  @ApiModelProperty(value = "The consumer’s access token key")
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Session nonce(String nonce) {
    this.nonce = nonce;
    return this;
  }

   /**
   * Random string, uniquely generated by the proxy
   * @return nonce
  **/
  @ApiModelProperty(value = "Random string, uniquely generated by the proxy")
  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public Session signature(String signature) {
    this.signature = signature;
    return this;
  }

   /**
   * Checksum generated by hmac-sha1 of accessToken + device.serialNumber + endpointId + nonce + invoiceId + amountCentis + currency
   * @return signature
  **/
  @ApiModelProperty(value = "Checksum generated by hmac-sha1 of accessToken + device.serialNumber + endpointId + nonce + invoiceId + amountCentis + currency")
  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public Session token(String token) {
    this.token = token;
    return this;
  }

   /**
   * The session token of transfer transaction
   * @return token
  **/
  @ApiModelProperty(value = "The session token of transfer transaction")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Session session = (Session) o;
    return Objects.equals(this.accessToken, session.accessToken) &&
        Objects.equals(this.nonce, session.nonce) &&
        Objects.equals(this.signature, session.signature) &&
        Objects.equals(this.token, session.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, nonce, signature, token);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Session {\n");
    
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}
