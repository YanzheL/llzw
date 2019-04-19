package com.llzw.apigate.spring;

import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.AddressBean;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Role;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MockEntityFactory {

  public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static Address makeAddress(Long id, User owner, AddressBean addressBean) throws Exception {
    Address address = (Address) addressBean;
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
    product.setSeller(seller);
    product.setName(String.format("test_product_name_%d", i));
    product.setIntroduction(String.format("test_product_introduction_%d", i));
    product.setPrice(i + 10000);
    product.setCa(String.format("test_product_ca_%d", i));
    product.setCaFile(String.format("test_product_caFile_%d", i));
    product.setCaId(String.format("test_product_caId_%d", i));
    product.setValid(true);
    return product;
  }

  public static Order makeOrder(Long id, Stock stock, User customer)
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
            String.format("Province_%d", id),
            String.format("City_%d", id),
            String.format("District_%d", id),
            String.format("Address_%d", id),
            String.format("Zip_%d", id)
        )
    );
    return order;
  }

  public static Stock makeStock(Long id, Product product)
      throws NoSuchFieldException, IllegalAccessException {
    Stock stock = new Stock();
    if (id != null) {
      setId(stock, id);
    }
    stock.setProduct(product);
    stock.setProducedAt(new Date());
    stock.setShelfLife(365);
    stock.setTotalQuantity(100);
    stock.setCurrentQuantity(100);
//    stock.setCreatedAt(new Date());
    stock.setValid(true);
    return stock;
  }

  public static User makeUser(String prefix, long id, Role role) {
    User user = new User();
    user.setUsername(String.format("%s_username_%d", prefix, id));
    user.setPassword(
        passwordEncoder.encode(String.format("%s_password_%d", prefix, id)));
    user.setNickname(String.format("%s_nickname_%d", prefix, id));
    user.setEmail(String.format("%s_email_%d@test.org", prefix, id));
    user.setPhoneNumber(String.format("1860000000%d", id));
    user.setEnabled(true);
    if (role != null) {
      user.setRoles(Collections.singleton(role));
    }
    return user;
  }

  public static <T> void setId(T obj, long id)
      throws NoSuchFieldException, IllegalAccessException {
    Field idField = obj.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(obj, id);
  }
}
