package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository
    extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  //PagingAndSortingRepository继承CrudRepository，支持实现分页排序相关方法
}
