package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import java.util.Date;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository
    extends PagingAndSortingRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

  int countAllByProductAndCreatedAtAfter(Product product, Date date);
}
