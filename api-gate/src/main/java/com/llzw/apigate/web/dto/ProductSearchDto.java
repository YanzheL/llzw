package com.llzw.apigate.web.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductSearchDto {

  @Size(max = 30, message = "Length must not exceed 30")
  protected String name;

  protected String introduction;

  @Size(max = 50, message = "Length must not exceed 50")
  protected String ca;

  @Size(max = 50, message = "Length must not exceed 50")
  protected String caId;

  @Size(max = 100, message = "Length must not exceed 100")
  protected String searchString;
}
