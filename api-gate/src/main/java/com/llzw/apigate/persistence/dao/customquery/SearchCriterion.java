package com.llzw.apigate.persistence.dao.customquery;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class SearchCriterion {

  private String key;
  private String operation;
  private Object value;
}
