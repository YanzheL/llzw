package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidParameterException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.JpaSearchSpecificationFactory;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockSearchDto;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultStockService implements StockService {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Override
  public Stock create(Long productId, Date producedAt, int shelfLife, int totalQuantity,
      String trackingId, String carrierName) throws RestApiException {
    Product product = productRepository.findById(productId).orElseThrow(() ->
        new RestDependentEntityNotFoundException(
            String.format("Product <%s> do not exist", productId)));
    Stock stock = new Stock();
    stock.setProduct(product);
    stock.setProducedAt(producedAt);
    stock.setShelfLife(shelfLife);
    stock.setTotalQuantity(totalQuantity);
    stock.setCurrentQuantity(totalQuantity);
    stock.setTrackingId(trackingId);
    stock.setCarrierName(carrierName);
    stock.setValid(true);
    return stockRepository.save(stock);
  }

  @Override
  public List<Stock> search(User owner, StockSearchDto dto,
      PageRequest pageRequest) throws RestApiException {
    try {
      // Results may contain other user's stocks, so we should filter them out.
      return stockRepository
          .findAll(
              JpaSearchSpecificationFactory.fromExample(dto),
              pageRequest
          )
          .getContent().stream()
          .filter(o -> o.belongsToSeller(owner))
          .collect(Collectors.toList());
    } catch (IllegalAccessException e) {
      throw new RestInvalidParameterException(e.getMessage());
    }
  }

  @Override
  public Stock findById(User owner, Long id) throws RestApiException {

    Stock stock = stockRepository.findById(id)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Stock <%s> does not exist", id)));
    if (!stock.belongsToSeller(owner)) {
      throw new RestAccessDeniedException("You do not have access to this entity");
    }
    return stock;
  }

  @Override
  public List<Stock> lockStocksForProduct(Product product, int quantity)
      throws RestApiException {
    AtomicReference<Integer> lockedItems = new AtomicReference<>();
    lockedItems.set(0);
    try (Stream<Stock> stockStream = stockRepository
        .findByProductAndInboundedAtNotNullAndValidTrueOrderByInboundedAt(product)) {
      return stockStream
          .takeWhile(stock -> {
            int current = lockedItems.get();
            int remain = quantity - current;
            if (current >= quantity) {
              return false;
            }
            if (stock.getCurrentQuantity() >= remain) {
              stock.decreaseCurrentQuantity(remain);
              lockedItems.set(current + remain);
            } else {
              lockedItems.set(current + stock.getCurrentQuantity());
              stock.setCurrentQuantity(0);
            }
            return true;
          })
          .map(stockRepository::save)
          .collect(Collectors.toList());
    }
  }

  @Override
  public int countAvailableStocks(Product product) {
    return stockRepository.countAllByProductAndInboundedAtNotNullAndValidTrue(product);
  }

  @Override
  public Stock save(Stock stock) {
    return stockRepository.save(stock);
  }
}
