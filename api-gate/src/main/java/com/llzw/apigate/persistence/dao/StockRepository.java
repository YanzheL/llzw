package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.stream.Stream;

public interface StockRepository
    extends PagingAndSortingRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
  // SELECT * FROM stocks WHERE product_id == ?1 AND inbounded_at IS NOT NULL ORDER BY inbounded_at
  Stream<Stock> findByProductIdAndInboundedAtNotNullOrderByInboundedAt(Product product);

  // SELECT * FROM stocks WHERE product_id == ?1 AND inbounded_at IS NOT NULL AND current_quantity >
  // ?2 ORDER BY inbounded_at
  Stream<Stock>
      findByProductIdAndInboundedAtNotNullAndCurrentQuantityGreaterThanEqualOrderByInboundedAt(
          Product product, int quantity);

  // SELECT * FROM stocks WHERE product_id == ?1 AND inbounded_at IS NULL ORDER BY inbounded_at
  Stream<Stock> findByProductIdAndInboundedAtIsNullOrderById(Product product);
}
