package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentRepository
    extends PagingAndSortingRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

  Optional<Payment> findByOrderId(UUID orderId);

}
