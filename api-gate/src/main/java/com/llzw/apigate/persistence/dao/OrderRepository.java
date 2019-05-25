package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository
    extends PagingAndSortingRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

  int countByProductAndCreatedAtAfter(Product product, Date date);

  @Query("SELECT o FROM Order o WHERE o.id = ?1 AND (o.product.seller = ?2 OR o.customer = ?2)")
  Optional<Order> findByIdAndUser(UUID id, User user);
}
