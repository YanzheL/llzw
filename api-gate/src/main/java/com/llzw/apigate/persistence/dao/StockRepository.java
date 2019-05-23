package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository
    extends PagingAndSortingRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

  @Query("SELECT SUM(s.currentQuantity) FROM Stock s WHERE s.product = ?1 AND s.inboundedAt IS NOT NULL AND s.valid = true")
  int countAllByProductAndInboundedAtNotNullAndValidTrue(Product product);

  // SELECT * FROM stocks
  // WHERE product == ?1 AND inbounded_at IS NOT NULL AND valid = true AND current_quantity > ?2
  // ORDER BY inbounded_at
  Stream<Stock> findByProductAndInboundedAtNotNullAndValidTrueOrderByInboundedAt(Product product);

  Optional<Stock> findByIdAndProductSeller(Long id, User user);
}
