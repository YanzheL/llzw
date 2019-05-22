package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.OrderPatchDto;
import com.llzw.apigate.web.dto.OrderSearchDto;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  Order create(User customer, Long productId, int quantity, Long addressId, String remark)
      throws RestApiException;

  List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable)
      throws RestApiException;

  Order get(String id, User relatedUser) throws RestApiException;

  Order cancel(String id, User relatedUser) throws RestApiException;

  Order patch(String id, OrderPatchDto dto, User relatedUser) throws RestApiException;

  Order deliveryConfirm(String id, User relatedUser) throws RestApiException;

  int countOrdersAfter(Product product, Date date);
}
