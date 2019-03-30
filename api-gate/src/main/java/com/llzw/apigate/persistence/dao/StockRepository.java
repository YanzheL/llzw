package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Stock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository
    extends PagingAndSortingRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {}
