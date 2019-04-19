package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.PaymentRepository;
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
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent>, Ordered {

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  @Setter(onMethod_ = @Autowired)
  private FileMetaDataRepository fileMetaDataRepository;

  @Setter(onMethod_ = @Autowired)
  private OrderRepository orderRepository;

  @Setter(onMethod_ = @Autowired)
  private PaymentRepository paymentRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Setter(onMethod_ = @Autowired)
  private UserRepository userRepository;

  @Setter(onMethod_ = @Autowired)
  private RoleRepository roleRepository;

  private static boolean alreadySetup = false;

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  @Transactional
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
    Role sellerRole = roleRepository.findByRole(RoleType.ROLE_SELLER)
        .orElseThrow(() -> new Exception("Seller role not found"));
    Role customerRole = roleRepository.findByRole(RoleType.ROLE_CUSTOMER)
        .orElseThrow(() -> new Exception("Customer role not found"));
    for (int i = 0; i < 10; ++i) {
      userRepository.save(
          MockEntityFactory.makeUser(String.format("test_user_%s", "seller"), i, sellerRole)
      );
    }
    for (int i = 0; i < 10; ++i) {
      userRepository.save(
          MockEntityFactory.makeUser(String.format("test_user_%s", "customer"), i, customerRole)
      );
    }
    for (int i = 0; i < 10; ++i) {
      userRepository.save(
          MockEntityFactory.makeUser(String.format("test_user_%s", "unknown"), i, null)
      );
    }
  }

  private void initProducts() throws Exception {
    for (int i = 0; i < 10; ++i) {
      User seller = userRepository
          .findByUsername(String.format("test_user_seller_username_%d", i))
          .orElseThrow(() -> new Exception("Test product seller not found"));
      productRepository.save(MockEntityFactory.makeTestProduct(null, i, seller));
    }
  }

  private void initStocks() throws Exception {
    for (int i = 0; i < 10; ++i) {
      Product product = productRepository.findById((long) (i + 1))
          .orElseThrow(() -> new Exception("product not found"));
      stockRepository.save(
          MockEntityFactory.makeStock(null, product, null)
      );
    }
    for (int i = 0; i < 10; ++i) {
      Product product = productRepository.findById((long) (i + 1))
          .orElseThrow(() -> new Exception("product not found"));
      stockRepository.save(
          MockEntityFactory.makeStock(null, product, new Date())
      );
    }
  }

  private void initAddresses() throws Exception {
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
