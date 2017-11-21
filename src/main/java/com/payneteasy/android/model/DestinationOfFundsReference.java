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
import java.io.IOException;

/**
 * DestinationOfFundsReference
 */

public class DestinationOfFundsReference {
  @SerializedName("clientCardId")
  private String clientCardId = null;

  public DestinationOfFundsReference clientCardId(String clientCardId) {
    this.clientCardId = clientCardId;
    return this;
  }

   /**
   * The destination card reference identifier (on client side)
   * @return clientCardId
  **/
  @ApiModelProperty(value = "The destination card reference identifier (on client side)")
  public String getClientCardId() {
    return clientCardId;
  }

  public void setClientCardId(String clientCardId) {
    this.clientCardId = clientCardId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DestinationOfFundsReference destinationOfFundsReference = (DestinationOfFundsReference) o;
    return Objects.equals(this.clientCardId, destinationOfFundsReference.clientCardId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientCardId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DestinationOfFundsReference {\n");
    
    sb.append("    clientCardId: ").append(toIndentedString(clientCardId)).append("\n");
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

