package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentRepository
    extends PagingAndSortingRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

  Optional<Payment> findByOrderId(UUID orderId);

  @Query(
      "SELECT p FROM Payment p WHERE p.order.id = ?1 AND (p.payer = ?2 OR p.order.product.seller = ?2)"
  )
  Optional<Payment> findByOrderIdAndUser(UUID orderId, User user);

  @Query(
      "SELECT p FROM Payment p WHERE p.id = ?1 AND (p.payer = ?2 OR p.order.product.seller = ?2)"
  )
  Optional<Payment> findByIdAndUser(Long id, User user);

}
