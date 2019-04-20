package com.llzw.apigate.service;

import com.llzw.apigate.service.error.RestApiException;

public interface ProductService {

  boolean updateValid(Long id) throws RestApiException;
}