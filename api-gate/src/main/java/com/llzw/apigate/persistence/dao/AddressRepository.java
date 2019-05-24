package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository
    extends PagingAndSortingRepository<Address, Long>, JpaSpecificationExecutor<Address> {

  List<Address> findAllByOwner(User owner, Pageable pageable);

  Optional<Address> findByIdAndOwner(Long id, User user);
}
