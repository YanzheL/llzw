package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository
    extends PagingAndSortingRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

}
