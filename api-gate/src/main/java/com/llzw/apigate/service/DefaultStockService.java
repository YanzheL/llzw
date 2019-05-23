package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
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
  public Stock create(User owner, Long id,
      Date producedAt, int shelfLife, int totalQuantity,
      String trackingId, String carrierName) throws RestApiException {
    Product product = productRepository.findByIdAndSeller(id, owner)
        .orElseThrow(
            () -> new RestDependentEntityNotFoundException(
                String
                    .format("Product <%s> does not exist or you do not have access to this entity",
                        id)
            )
        );
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
  public List<Stock> search(User owner, StockSearchDto dto, PageRequest pageRequest) {
    return stockRepository
        .findAll(
            makeSpec(dto, owner),
            pageRequest
        )
        .getContent();
  }

  @Override
  public Stock findById(User owner, Long id) throws RestApiException {
    return stockRepository.findByIdAndProductSeller(id, owner)
        .orElseThrow(
            () -> new RestEntityNotFoundException(
                String
                    .format("Stock <%s> does not exist or you do not have access to this entity",
                        id)
            )
        );
  }

  @Override
  public List<Stock> lockStocksForProduct(Product product, int quantity) {
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
    Specification<Stock> specification = Stock.belongsToSellerSpec(relatedUser);
    specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> expressions = new ArrayList<>();
      if (dto.getProductId() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("product"), dto.getProductId()
        ));
      }
      if (dto.getCarrierName() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("carrierName"), dto.getCarrierName()
        ));
      }
      if (dto.getTrackingId() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("trackingId"), dto.getTrackingId()
        ));
      }
      if (dto.getShelfLife() != null) {
        expressions.add(criteriaBuilder.equal(
            root.get("shelfLife"), dto.getShelfLife()
        ));
      }
      if (dto.getValid() != null) {
        expressions.add(
            criteriaBuilder.equal(
                root.get("valid"), dto.getValid()
            )
        );
      }
      return expressions.stream().reduce(criteriaBuilder::and).orElse(null);
    });
    return specification;
  }
}
