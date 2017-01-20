package io.pivotal.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.springframework.http.HttpStatus;

public class BaseError {
  @JsonIgnore
  private HttpStatus httpStatus;
  private String message;
  public BaseError() {
  }
  public BaseError(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
  @JsonProperty(access = Access.READ_ONLY)
  public int getStatus() {
    return httpStatus.value();
  }
  @JsonIgnore
  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
  @JsonIgnore
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
}