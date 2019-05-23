package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockSearchDto;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface StockService {

  Stock create(
      Long productId,
      Date producedAt,
      int shelfLife,
      int totalQuantity,
      String trackingId,
      String carrierName
  ) throws RestApiException;

  List<Stock> search(User owner, StockSearchDto dto,
      PageRequest pageRequest) throws RestApiException;

  Stock findById(User owner, Long id) throws RestApiException;

  List<Stock> lockStocksForProduct(Product product, int quantity) throws RestApiException;

  int countAvailableStocks(Product product);

  Stock save(Stock stock);

}
