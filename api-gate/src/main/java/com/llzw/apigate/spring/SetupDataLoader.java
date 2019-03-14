package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.dao.RoleRepository;
import com.llzw.apigate.persistence.entity.Privilege;
import com.llzw.apigate.persistence.entity.Privilege.PrivilegeType;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.Role.RoleType;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  private static Map<RoleType, Collection<PrivilegeType>> defaultRoleMapping = new HashMap<>();

  static {
    defaultRoleMapping.put(
        RoleType.SELLER,
        Arrays.asList(
            PrivilegeType.CREATE_PRODUCT,
            PrivilegeType.CREATE_STOCK,
            PrivilegeType.READ_ORDER,
            PrivilegeType.READ_PRODUCT,
            PrivilegeType.READ_STOCK,
            PrivilegeType.UPDATE_PRODUCT,
            PrivilegeType.UPDATE_STOCK,
            PrivilegeType.DELETE_ORDER,
            PrivilegeType.DELETE_PRODUCT,
            PrivilegeType.DELETE_STOCK,
            PrivilegeType.MANAGE_PASSWORD));

    defaultRoleMapping.put(
        RoleType.CUSTOMER,
        Arrays.asList(
            PrivilegeType.CREATE_ORDER,
            PrivilegeType.READ_ORDER,
            PrivilegeType.READ_PRODUCT,
            PrivilegeType.MANAGE_PASSWORD));
  }

  // API

  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    for (Map.Entry<RoleType, Collection<PrivilegeType>> entry : defaultRoleMapping.entrySet()) {
      RoleType role = entry.getKey();
      Collection<Privilege> privileges =
          entry.getValue().stream().map(Privilege::new).collect(Collectors.toList());
      createRoleIfNotFound(role, privileges);
    }

    alreadySetup = true;
  }

  @Transactional
  protected void createRoleIfNotFound(RoleType type, final Collection<Privilege> privileges) {
    Role role = roleRepository.findByRole(type).orElse(new Role(type));
    role.setPrivileges(privileges);
    roleRepository.save(role);
  }
}
