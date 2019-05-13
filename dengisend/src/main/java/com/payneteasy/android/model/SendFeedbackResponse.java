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
import com.payneteasy.android.model.Error;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;

/**
 * SendFeedbackResponse
 */

public class SendFeedbackResponse {

  @SerializedName("error")
  private Error error = null;
  public SendFeedbackResponse error(Error error) {
    this.error = error;
    return this;
  }

  

  /**
  * Get error
  * @return error
  **/
  @Schema(description = "")
  public Error getError() {
    return error;
  }
  public void setError(Error error) {
    this.error = error;
  }
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendFeedbackResponse sendFeedbackResponse = (SendFeedbackResponse) o;
    return Objects.equals(this.error, sendFeedbackResponse.error);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendFeedbackResponse {\n");
    
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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
