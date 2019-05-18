package com.llzw.apigate.web.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductSearchDto {

  @Size(max = 30, message = "Length must not exceed 30")
  protected String name;

  protected String introduction;

  @Size(max = 100, message = "Length must not exceed 100")
  protected String global;

  protected boolean valid = true;

  @Size(max = 255, message = "Length of category should not exceed 255")
  protected String category;

  @Size(max = 255, message = "Length of feature should not exceed 255")
  protected String feature;
}
