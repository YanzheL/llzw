package com.llzw.apigate.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class ProductStat implements Serializable {

  @JsonIgnore
  protected boolean stocksOutDated;

  @JsonIgnore
  protected boolean salesOutDated;

  protected int salesLastMonth;

  protected int currentStocks;
}
