package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Payment;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@ActiveProfiles("dev")
//@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AlipayServiceTests {

  @Autowired
  AlipayService alipayService;

  @Test
  public void payTest() throws Exception {
    Payment payment = new Payment();
    Order order = new Order();
    Field idField = Order.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(order, 2L);
    payment.setOrder(order);
    payment.setSubject("路虎");
    payment.setTotalAmount(100000.1f);
    payment.setDescription("dec test");
    String orderString = alipayService.pay(payment);
    System.out.println(orderString);
  }

  @Test
  public void queryTest() throws Exception {
    Map<String, String> body = alipayService.query(2L);
    System.out.println(body);
  }
}
