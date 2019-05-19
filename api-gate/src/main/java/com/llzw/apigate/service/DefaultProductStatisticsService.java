package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.ProductStat;
import java.util.Calendar;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductStatisticsService implements ProductStatisticsService {

  @Setter(onMethod_ = @Autowired)
  protected ProductService productService;

  @Setter(onMethod_ = @Autowired)
  protected OrderService orderService;

  @Setter(onMethod_ = @Autowired)
  protected StockService stockService;

  @Override
  public void updateStat(Product product) {
    ProductStat stat = product.getStat();
    boolean updated = false;
    if (stat.isStocksOutDated()) {
      stat.setCurrentStocks(
          stockService.countAvailableStocks(product)
      );
      stat.setStocksOutDated(false);
      updated = true;
    }
    if (stat.isSalesOutDated()) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MONTH, -1);
      stat.setSalesLastMonth(
          orderService.countOrdersAfter(
              product,
              calendar.getTime()
          )
      );
      stat.setSalesOutDated(false);
      updated = true;
    }
    if (updated) {
      productService.save(product);
    }
  }
}
