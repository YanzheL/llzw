package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidParameterException;
import com.llzw.apigate.message.error.RestRejectedByEntityException;
import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import com.llzw.apigate.persistence.entity.AddressBean;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.OrderSearchDto;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultOrderService implements OrderService {

  @Setter(onMethod_ = @Autowired)
  private OrderRepository orderRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @Override
  public Order create(User customer, Long productId, int quantity, Long addressId)
      throws RestApiException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Product <%s> does not exist", productId)));
    AddressBean addressBean = addressRepository.findById(addressId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Address <%s> do not exist", addressId)));
    Stock stock;
    try (Stream<Stock> validStocks =
        stockRepository
            .findByProductAndInboundedAtNotNullAndCurrentQuantityGreaterThanEqualOrderByInboundedAt(
                product, quantity)) {
      stock = validStocks.findFirst().orElseThrow(() -> new RestDependentEntityNotFoundException(
          "Cannot find an available stock specifies that quantity"));
    }
    stock.decreaseCurrentQuantity(quantity);
    Order order = new Order();
    order.setStock(stockRepository.save(stock));
    order.setAddress(addressBean);
    order.setCustomer(customer);
    order.setQuantity(quantity);
    order.setValid(true);
    return orderRepository.save(order);
  }

  @Override
  public List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable)
      throws RestApiException {
    try {
      // Result orders may contain other user's order, so we should filter them out.
      return orderRepository
          .findAll(SearchCriterionSpecificationFactory.fromExample(example), pageable)
          .getContent().stream()
          .filter(o -> o.belongsToUser(relatedUser))
          .collect(Collectors.toList());
    } catch (IllegalAccessException e) {
      throw new RestInvalidParameterException(e.getMessage());
    }
  }

  @Override
  public Order get(Long id, User relatedUser) throws RestApiException {
    Order order = orderRepository.findById(id).orElseThrow(() -> new RestEntityNotFoundException(
        String.format("Order <%s> does not exist", id)));
    if (!order.belongsToUser(relatedUser)) {
      throw new RestAccessDeniedException("Current user does not have access to this order");
    }
    return order;
  }

  @Override
  public Order cancel(Long id, User relatedUser) throws RestApiException {
    Order order = get(id, relatedUser);
    if (order.getShippingTime() != null) {
      throw new RestRejectedByEntityException(
          String.format("Order <%d> has already been shipped", id));
    }
    order.setValid(false);
    return orderRepository.save(order);
  }

  @Override
  public Order deliveryConfirm(Long id, User relatedUser) throws RestApiException {
    Order order = get(id, relatedUser);
    if (order.isDeliveryConfirmed()) {
      throw new RestRejectedByEntityException(
          String.format("Order <%d> has already been confirmed", id));
    }
    order.setDeliveryConfirmed(true);
    return orderRepository.save(order);
  }
}
