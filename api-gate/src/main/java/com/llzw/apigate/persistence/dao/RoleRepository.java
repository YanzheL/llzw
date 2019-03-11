package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByRole(Role.RoleType type);

  void deleteByRole(Role.RoleType type);
}
