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
 * RatesResponse
 */

public class RatesResponse {

  @SerializedName("rateInterest")
  private Double rateInterest = null;

  @SerializedName("rateMin")
  private Double rateMin = null;

  @SerializedName("rateMax")
  private Double rateMax = null;

  @SerializedName("limitMin")
  private Double limitMin = null;

  @SerializedName("limitMax")
  private Double limitMax = null;

  @SerializedName("error")
  private Error error = null;
  public RatesResponse rateInterest(Double rateInterest) {
    this.rateInterest = rateInterest;
    return this;
  }

  

  /**
  * Transfer rate interest (percent)
  * @return rateInterest
  **/
  @Schema(description = "Transfer rate interest (percent)")
  public Double getRateInterest() {
    return rateInterest;
  }
  public void setRateInterest(Double rateInterest) {
    this.rateInterest = rateInterest;
  }
  public RatesResponse rateMin(Double rateMin) {
    this.rateMin = rateMin;
    return this;
  }

  

  /**
  * Minimum commission amount
  * @return rateMin
  **/
  @Schema(description = "Minimum commission amount")
  public Double getRateMin() {
    return rateMin;
  }
  public void setRateMin(Double rateMin) {
    this.rateMin = rateMin;
  }
  public RatesResponse rateMax(Double rateMax) {
    this.rateMax = rateMax;
    return this;
  }

  

  /**
  * Maximum commission amount
  * @return rateMax
  **/
  @Schema(description = "Maximum commission amount")
  public Double getRateMax() {
    return rateMax;
  }
  public void setRateMax(Double rateMax) {
    this.rateMax = rateMax;
  }
  public RatesResponse limitMin(Double limitMin) {
    this.limitMin = limitMin;
    return this;
  }

  

  /**
  * Minimum transfer amount
  * @return limitMin
  **/
  @Schema(description = "Minimum transfer amount")
  public Double getLimitMin() {
    return limitMin;
  }
  public void setLimitMin(Double limitMin) {
    this.limitMin = limitMin;
  }
  public RatesResponse limitMax(Double limitMax) {
    this.limitMax = limitMax;
    return this;
  }

  

  /**
  * Maximum transfer amount
  * @return limitMax
  **/
  @Schema(description = "Maximum transfer amount")
  public Double getLimitMax() {
    return limitMax;
  }
  public void setLimitMax(Double limitMax) {
    this.limitMax = limitMax;
  }
  public RatesResponse error(Error error) {
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
    RatesResponse ratesResponse = (RatesResponse) o;
    return Objects.equals(this.rateInterest, ratesResponse.rateInterest) &&
        Objects.equals(this.rateMin, ratesResponse.rateMin) &&
        Objects.equals(this.rateMax, ratesResponse.rateMax) &&
        Objects.equals(this.limitMin, ratesResponse.limitMin) &&
        Objects.equals(this.limitMax, ratesResponse.limitMax) &&
        Objects.equals(this.error, ratesResponse.error);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(rateInterest, rateMin, rateMax, limitMin, limitMax, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatesResponse {\n");
    
    sb.append("    rateInterest: ").append(toIndentedString(rateInterest)).append("\n");
    sb.append("    rateMin: ").append(toIndentedString(rateMin)).append("\n");
    sb.append("    rateMax: ").append(toIndentedString(rateMax)).append("\n");
    sb.append("    limitMin: ").append(toIndentedString(limitMin)).append("\n");
    sb.append("    limitMax: ").append(toIndentedString(limitMax)).append("\n");
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
