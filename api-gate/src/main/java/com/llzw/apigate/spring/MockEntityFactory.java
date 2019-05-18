package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.AddressBean;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MockEntityFactory {

  public static Random randomGenerator = new Random();

  public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static Address makeAddress(Long id, User owner, AddressBean addressBean) throws Exception {
    Address address = new Address();
    BeanUtils.copyProperties(addressBean, address);
    if (id != null) {
      setId(address, id);
    }
    address.setOwner(owner);
    return address;
  }

  public static Product makeTestProduct(Long id, int i, User seller) throws Exception {
    Product product = new Product();
    if (id != null) {
      setId(product, id);
    }
    List<String> categories = Arrays.asList(
        "food.seafood.shrimp",
        "food.seafood.fish",
        "food.seafood.lobster",
        "food.meat.pork",
        "food.meat.beef",
        "food.meat.mutton",
        "food.vegetable"
    );
    List<String> features = Arrays.asList(
        "包邮",
        "促销",
        "新鲜"
    );
    product.setSeller(seller);
    product.setName(String.format("澳门特产 %d", i));
    product.setIntroduction(String.format("澳门Morpheus酒店独家供应 %d", i));
    product.setPrice(i + 10000);
    product.setCa(String.format("test_product_ca_%d", i));
    product.setCaFile(String.format("test_product_caFile_%d", i));
    product.setCaId(String.format("test_product_caId_%d", i));
    product.setCategory(categories.get(randomGenerator.nextInt(categories.size())));
    product.setFeature(features.get(randomGenerator.nextInt(features.size())));
    product.setValid(true);
    return product;
  }

  public static Order makeOrder(UUID id, Stock stock, User customer)
      throws NoSuchFieldException, IllegalAccessException {
    Order order = new Order();
    if (id != null) {
      setId(order, id);
    }
    order.setStock(stock);
    order.setQuantity(10);
    order.setTotalAmount(1000.123f);
    order.setCustomer(customer);
//    order.setCreatedAt(new Date());
    order.setValid(true);
    order.setAddress(
        new AddressBean(
            String.format("Province_%s", id.toString().subSequence(0, 4)),
            String.format("City_%s", id.toString().subSequence(0, 4)),
            String.format("District_%s", id.toString().subSequence(0, 4)),
            String.format("Address_%s", id.toString().subSequence(0, 4)),
            String.format("000000", id.toString().subSequence(0, 4))
        )
    );
    return order;
  }

  public static Stock makeStock(Long id, Product product, Date inboundedAt)
      throws NoSuchFieldException, IllegalAccessException {
    Stock stock = new Stock();
    if (id != null) {
      setId(stock, id);
    }
    stock.setProduct(product);
    stock.setProducedAt(new Date());
    stock.setShelfLife(365);
    stock.setTotalQuantity(100000);
    stock.setCurrentQuantity(100000);
    stock.setInboundedAt(inboundedAt);
//    stock.setCreatedAt(new Date());
    stock.setValid(true);
    return stock;
  }

  public static User makeUser(String prefix, long id, Role role) {
    User user = new User();
    user.setUsername(String.format("%s_username_%d", prefix, id));
    user.setPassword(
        passwordEncoder.encode(String.format("%s_PASSWORD_%d", prefix, id)));
    user.setNickname(String.format("%s_nickname_%d", prefix, id));
    user.setEmail(String.format("%s_email_%d@test.org", prefix, id));
    user.setPhoneNumber(String.format("1860000000%d", id));
    user.setEnabled(true);
    if (role != null) {
      user.setRoles(Collections.singleton(role));
    }
    return user;
  }

  public static <T, ID> void setId(T obj, ID id)
      throws NoSuchFieldException, IllegalAccessException {
    Field idField = obj.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(obj, id);
  }
}
