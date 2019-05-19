package com.llzw.apigate.web.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/*
 * 把在一个接口中需要的所有必要参数全部放到一个POJO中，包括从数据库中获取的和从用户处得到的。
 * 从开始接收用户数据开始，一直都使用这一个数据传输对象，可以有效避免代码参数过多变得特别凌乱不好维护的场景。
 * 同时，写成dto后可以有效的把逻辑层剥离出来。
 * */
@Data
public class ProductCreateDto {

  @NotNull(message = "the name of the product can't be empty")
  @Size(min = 1, max = 255, message = "Length should between 1 to 255")
  protected String name;

  @NotNull(message = "introduction can't be empty")
  protected String introduction;

  @NotNull(message = "price can't be empty")
  protected Float price;

  @Size(max = 9, message = "Length of images should not exceed 9")
  protected List<String> mainImageFiles;

  @NotNull
  @Size(min = 1, max = 50, message = "Length must between 1 and 50")
  protected String ca;

  @NotNull
  @Size(min = 1, max = 50, message = "Length must between 1 and 50")
  protected String caId;

  @NotNull
  @Size(min = 64, max = 64, message = "Length must be 64")
  protected String caFile;

  @Size(max = 255, message = "Length of category should not exceed 255")
  protected String category;

  @Size(max = 255, message = "Length of feature should not exceed 255")
  protected String feature;
}
