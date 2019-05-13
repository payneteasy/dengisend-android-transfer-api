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
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;

/**
 * SourceOfFundsCardHolder
 */

public class SourceOfFundsCardHolder {

  @SerializedName("firstName")
  private String firstName = null;

  @SerializedName("lastName")
  private String lastName = null;
  public SourceOfFundsCardHolder firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  

  /**
  * Cardholder’s first name
  * @return firstName
  **/
  @Schema(required = true, description = "Cardholder’s first name")
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public SourceOfFundsCardHolder lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  

  /**
  * Cardholder’s last name
  * @return lastName
  **/
  @Schema(required = true, description = "Cardholder’s last name")
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SourceOfFundsCardHolder sourceOfFundsCardHolder = (SourceOfFundsCardHolder) o;
    return Objects.equals(this.firstName, sourceOfFundsCardHolder.firstName) &&
        Objects.equals(this.lastName, sourceOfFundsCardHolder.lastName);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(firstName, lastName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SourceOfFundsCardHolder {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
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
