package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.PaymentRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.RoleRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Role.RoleType;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import java.util.Collections;
import java.util.Date;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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

  @Setter(onMethod_ = @Autowired)
  private PasswordEncoder passwordEncoder;

  private String fieldPrefix = "test";

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    try {
      initUsers();
      initProducts();
      initStocks();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initUsers() {
    for (int i = 0; i < 10; ++i) {
      userRepository.save(makeTestUser("seller", i, RoleType.ROLE_SELLER));
    }
    for (int i = 0; i < 10; ++i) {
      userRepository.save(makeTestUser("customer", i, RoleType.ROLE_CUSTOMER));
    }
    for (int i = 0; i < 10; ++i) {
      userRepository.save(makeTestUser("unknown", i, null));
    }
  }

  private void initProducts() throws Exception {
    for (int i = 0; i < 10; ++i) {
      productRepository.save(makeTestProduct(i));
    }
  }

  private void initStocks() throws Exception {
    for (int i = 0; i < 10; ++i) {
      stockRepository.save(makeTestStock(i));
    }
  }

  private User makeTestUser(String prefix, int i, RoleType roleType) {
    User user = new User();
    user.setUsername(String.format("%s_user_%s_username_%d", fieldPrefix, prefix, i));
    user.setPassword(
        passwordEncoder.encode(String.format("%s_user_%s_password_%d", fieldPrefix, prefix, i)));
    user.setNickname(String.format("%s_user_%s_nickname_%d", fieldPrefix, prefix, i));
    user.setEmail(String.format("%s_user_%s_email_%d@test.org", fieldPrefix, prefix, i));
    user.setPhoneNumber(String.format("1860000000%d", i));
    user.setEnabled(true);
    if (roleType != null) {
      user.setRoles(Collections.singleton(roleRepository.findByRole(roleType).get()));
    }
    return user;
  }

  private Product makeTestProduct(int i) throws Exception {
    User seller = userRepository
        .findByUsername(String.format("%s_user_seller_username_%d", fieldPrefix, i))
        .orElseThrow(Exception::new);
    Product product = new Product();
    product.setSeller(seller);
    product.setName(String.format("%s_product_name_%d", fieldPrefix, i));
    product.setIntroduction(String.format("%s_product_introduction_%d", fieldPrefix, i));
    product.setPrice(i + 10000);
    product.setCa(String.format("%s_product_ca_%d", fieldPrefix, i));
    product.setCaFile(String.format("%s_product_caFile_%d", fieldPrefix, i));
    product.setCaId(String.format("%s_product_caId_%d", fieldPrefix, i));
    product.setValid(true);
    return product;
  }

  private Stock makeTestStock(int i) throws Exception {
//    List<Product> all = Lists.newArrayList(productRepository.findAll());
    Product product = productRepository.findById((long) (i + 1)).orElseThrow(Exception::new);
    Stock stock = new Stock();
    stock.setProductId(product);
    stock.setProducedAt(new Date());
    stock.setShelfLife(i + 100);
    stock.setTotalQuantity(i + 100);
    stock.setCurrentQuantity(i + 100);
    stock.setValid(true);
    return stock;
  }
}
