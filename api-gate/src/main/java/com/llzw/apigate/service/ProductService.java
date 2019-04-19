package com.llzw.apigate.service;

import java.util.Collection;

public interface ProductService {
  boolean updateValid(Long id, Collection<String> msgs);
}