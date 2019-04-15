package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Privilege;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

  Optional<Privilege> findByPrivilege(Privilege.PrivilegeType name);

  void deleteByPrivilege(Privilege.PrivilegeType type);
}
