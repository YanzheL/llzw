package com.llzw.apigate.web.dto;

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
  @Size(min = 1, max = 30, message = "Length should between 1 to 30")
  protected String name;

  @NotNull(message = "introduction can't be empty")
  @Size(min = 1, max = 50, message = "Length should between 1 to 50")
  protected String introduction;

  @NotNull(message = "price can't be empty")
  protected Float price;

  @NotNull
  @Size(min = 1, max = 50, message = "Length should between 1 to 50")
  protected Float ca;

  @NotNull
  @Size(min = 1, max = 50, message = "Length should between 1 to 50")
  protected String caId;

}
