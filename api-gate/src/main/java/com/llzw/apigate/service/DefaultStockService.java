package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.StockSearchDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Predicate;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
    return stockRepository
        .findAll(
            makeSpec(dto, owner),
            pageRequest
        )
        .getContent();
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

  private Specification<Stock> makeSpec(StockSearchDto dto, User relatedUser) {
    Specification<Stock> specification = (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> expressions = new ArrayList<>();
      if (dto.getProductId() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("product").<Long>get("id"), dto.getProductId()
        ));
      }
      if (dto.getCarrierName() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("carrierName"), dto.getCarrierName()
        ));
      }
      if (dto.getShelfLife() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("shelfLife"), dto.getShelfLife()
        ));
      }
      if (dto.getTrackingId() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("trackingId"), dto.getTrackingId()
        ));
      }
      expressions.add(
          criteriaBuilder.equal(
              root.get("valid"), dto.isValid()
          )
      );
      Predicate expression = null;
//      expressions.stream().reduce()
      for (Predicate expr : expressions) {
        if (expression == null) {
          expression = expr;
        } else {
          expression = criteriaBuilder.and(expression, expr);
        }
      }
      return expression;
    };
    specification.and(Stock.belongsToSellerSpec(relatedUser));
    return specification;
  }
}
