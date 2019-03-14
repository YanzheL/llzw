package com.llzw.apigate.spring;

import com.google.common.collect.Lists;
import com.llzw.apigate.persistence.dao.PrivilegeRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private static Map<RoleType, Collection<PrivilegeType>> defaultRoleMapping = new HashMap<>();

  static {
    defaultRoleMapping.put(
        RoleType.ROLE_SELLER,
        Arrays.asList(
            PrivilegeType.OP_CREATE_PRODUCT,
            PrivilegeType.OP_CREATE_STOCK,
            PrivilegeType.OP_READ_ORDER,
            PrivilegeType.OP_READ_PRODUCT,
            PrivilegeType.OP_READ_STOCK,
            PrivilegeType.OP_UPDATE_PRODUCT,
            PrivilegeType.OP_UPDATE_STOCK,
            PrivilegeType.OP_DELETE_ORDER,
            PrivilegeType.OP_DELETE_PRODUCT,
            PrivilegeType.OP_DELETE_STOCK,
            PrivilegeType.OP_MANAGE_PASSWORD));

    defaultRoleMapping.put(
        RoleType.ROLE_CUSTOMER,
        Arrays.asList(
            PrivilegeType.OP_CREATE_ORDER,
            PrivilegeType.OP_READ_ORDER,
            PrivilegeType.OP_READ_PRODUCT,
            PrivilegeType.OP_MANAGE_PASSWORD));
  }

  private boolean alreadySetup = false;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  @Setter(onMethod_ = @Autowired)
  private PrivilegeRepository privilegeRepository;

  // API

  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    for (Map.Entry<RoleType, Collection<PrivilegeType>> entry : defaultRoleMapping.entrySet()) {
      RoleType role = entry.getKey();
      Collection<Privilege> allPrivileges = Lists.newArrayList(privilegeRepository.findAll());
      Set<Privilege> privileges =
          entry.getValue().stream()
              .map(
                  type -> {
                    for (Privilege p : allPrivileges) {
                      if (p.getPrivilege() == type) return p;
                    }
                    return new Privilege(type);
                  })
              .collect(Collectors.toSet());
      initRole(role, privileges);
    }

    alreadySetup = true;
  }

  @Transactional
  protected void initRole(RoleType type, Set<Privilege> privileges) {
    Role role = roleRepository.findByRole(type).orElse(new Role(type));
    Collection<Privilege> found = role.getPrivileges();
    privileges.forEach(
        p -> {
          if (!found.contains(p)) found.add(p);
        });
    roleRepository.save(role);
  }
}
