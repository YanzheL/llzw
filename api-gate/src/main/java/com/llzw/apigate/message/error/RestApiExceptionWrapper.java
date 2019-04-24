package com.llzw.apigate.message.error;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;

public class RestApiExceptionWrapper {

  private static final Map<Class<? extends Exception>, Class<? extends RestApiException>> mapping;

  static {
    mapping = new HashMap<>();
    mapping.put(ConversionNotSupportedException.class, RestInvalidParameterException.class);
    mapping.put(AccessDeniedException.class, RestAccessDeniedException.class);
    mapping.put(BindException.class, RestInvalidParameterException.class);
    mapping.put(EntityExistsException.class, RestEntityExistsException.class);
    mapping.put(UsernameNotFoundException.class, RestEntityNotFoundException.class);
    mapping.put(ConstraintViolationException.class, RestInvalidParameterException.class);
  }

  private RestApiExceptionWrapper() {
  }

  public static <T extends Exception> RestApiException wrap(T ex) {
    if (ex instanceof RestApiException) {
      return (RestApiException) ex;
    }
    try {
      Class<? extends Exception> cls = ex.getClass();
      for (Map.Entry<Class<? extends Exception>, Class<? extends RestApiException>> entry : mapping
          .entrySet()) {
        if (entry.getKey().equals(cls)) {
          Class<? extends RestApiException> targetCls = entry.getValue();
          return targetCls.getConstructor(String.class).newInstance(ex.getMessage());
        }
      }
      return new RestApiException(ex.getMessage());
    } catch (Exception e) {
      return new RestInternalServerException();
    }
  }

}
