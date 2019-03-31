package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Role;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByRole(Role.RoleType type);

  void deleteByRole(Role.RoleType type);
}
