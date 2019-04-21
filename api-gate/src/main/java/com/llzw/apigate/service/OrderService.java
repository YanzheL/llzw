package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.OrderSearchDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  Order create(User customer, Long productId, int quantity, Long addressId)
      throws RestApiException;

  List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable)
      throws RestApiException;

  Order get(Long id, User relatedUser) throws RestApiException;

  Order cancel(Long id, User relatedUser) throws RestApiException;

  Order deliveryConfirm(Long id, User relatedUser) throws RestApiException;
}
