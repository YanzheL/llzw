package com.llzw.apigate;

import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.RestApiException;
import com.llzw.apigate.web.dto.OrderSearchDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  Order create(User customer, Long productId, int quantity, Long addressId)
      throws RestApiException;

  List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable)
      throws RestApiException;

  Optional<Order> get(Long id);
}
