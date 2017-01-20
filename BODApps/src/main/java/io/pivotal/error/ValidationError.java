package io.pivotal.error;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

public class ValidationError extends BaseError {
  public ValidationError(final List<ObjectError> validationErrors) {
    super(HttpStatus.BAD_REQUEST, "Invalid query parameters.\n" + validationErrors.stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(".\n")));
  }
}