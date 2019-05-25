package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.RoleRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.AddressBean;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.Role.RoleType;
import com.llzw.apigate.persistence.entity.User;
import java.util.Date;
import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent>, Ordered {

  private static boolean alreadySetup = false;

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  @Setter(onMethod_ = @Autowired)
  private TestDataProperties testDataProperties;

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  @Transactional
  synchronized
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    if (alreadySetup) {
      return;
    }
    try {
      initUsers();
      initProducts();
      initStocks();
      initAddresses();
      alreadySetup = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    alreadySetup = true;
  }

  private void initUsers() throws Exception {
    if (!testDataProperties.loadUsers) {
      return;
    }
    Role sellerRole = roleRepository.findByRole(RoleType.ROLE_SELLER)
        .orElseThrow(() -> new Exception("Seller role not found"));
    Role customerRole = roleRepository.findByRole(RoleType.ROLE_CUSTOMER)
        .orElseThrow(() -> new Exception("Customer role not found"));
    for (int i = 0; i < 10; ++i) {
      String username = String.format("test_user_%s", "seller");
      if (userRepository.findByUsername(username).isPresent()) {
        continue;
      }
      userRepository.save(
          MockEntityFactory.makeUser(username, i, sellerRole)
      );
    }
    for (int i = 0; i < 10; ++i) {
      String username = String.format("test_user_%s", "customer");
      if (userRepository.findByUsername(username).isPresent()) {
        continue;
      }
      userRepository.save(
          MockEntityFactory.makeUser(username, i, customerRole)
      );
    }
    for (int i = 0; i < 10; ++i) {
      String username = String.format("test_user_%s", "unknown");
      if (userRepository.findByUsername(username).isPresent()) {
        continue;
      }
      userRepository.save(
          MockEntityFactory.makeUser(username, i, null)
      );
    }
  }

  private void initProducts() throws Exception {
    if (!testDataProperties.loadProducts) {
      return;
    }
    for (int i = 0; i < 10; ++i) {
      User seller = userRepository
          .findByUsername(String.format("test_user_seller_username_%d", i))
          .orElseThrow(() -> new Exception("Test product seller not found"));
      for (int j = 0; j < 10; ++j) {
        productRepository.save(MockEntityFactory.makeTestProduct(null, j, seller));
      }
    }
  }

  private void initStocks() throws Exception {
    if (!testDataProperties.loadStocks) {
      return;
    }
    for (int i = 0; i < 10; ++i) {
      List<Product> products = productRepository
          .findAllBySellerUsername(String.format("test_user_seller_username_%d", i));
      for (Product product : products) {
        for (int j = 0; j < 10; ++j) {
          stockRepository.save(
              MockEntityFactory.makeStock(null, product, null, 100)
          );
        }
        for (int j = 0; j < 10; ++j) {
          stockRepository.save(
              MockEntityFactory.makeStock(null, product, new Date(), 100)
          );
        }
      }
    }
  }

  private void initAddresses() throws Exception {
    if (!testDataProperties.loadAddresses) {
      return;
    }
    for (int i = 0; i < 10; ++i) {
      User seller = userRepository
          .findByUsername(String.format("test_user_seller_username_%d", i))
          .orElseThrow(() -> new Exception("Test product seller not found"));
      Address address = MockEntityFactory.makeAddress(
          null,
          seller,
          new AddressBean(
              String.format("test_province_%d", i),
              String.format("test_city_%d", i),
              String.format("test_district_%d", i),
              String.format("test_address_%d", i),
              String.format("00000%d", i)
          )
      );
      addressRepository.save(address);
    }
    for (int i = 0; i < 10; ++i) {
      User customer = userRepository
          .findByUsername(String.format("test_user_customer_username_%d", i))
          .orElseThrow(() -> new Exception("Test product customer not found"));
      Address address = MockEntityFactory.makeAddress(
          null,
          customer,
          new AddressBean(
              String.format("test_province_%d", i),
              String.format("test_city_%d", i),
              String.format("test_district_%d", i),
              String.format("test_address_%d", i),
              String.format("00000%d", i)
          )
      );
      addressRepository.save(address);
    }
  }
}
