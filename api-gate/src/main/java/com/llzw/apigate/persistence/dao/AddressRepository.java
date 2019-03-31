package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository
    extends PagingAndSortingRepository<Address, Long>, JpaSpecificationExecutor<Address> {

}
