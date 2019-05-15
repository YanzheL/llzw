package com.llzw.apigate.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.llzw.apigate.message.error.RestTradeNotFoundPaymentVendorException;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.spring.AlipayProperties;
import java.lang.reflect.Field;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {AlipayService.class, AlipayProperties.class},
    initializers = ConfigFileApplicationContextInitializer.class
)
@EnableConfigurationProperties
public class AlipayServiceTests {

  protected UUID testUUID = UUID.randomUUID();

  @Autowired
  AlipayService alipayService;

  @Test
  public void payTest() throws Exception {
    Payment payment = new Payment();
    Order order = new Order();
    Field idField = Order.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(order, testUUID);
    payment.setOrder(order);
    payment.setSubject("特斯拉");
    payment.setTotalAmount(1200000);
    payment.setDescription("这是一辆特斯拉");
    String orderString = alipayService.pay(payment);
    System.out.println(orderString);
  }

  @Test
  public void queryTest() throws Exception {
    assertThrows(
        RestTradeNotFoundPaymentVendorException.class,
        () -> alipayService.query(testUUID.toString())
    );
  }
}
