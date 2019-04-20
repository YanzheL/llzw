package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestInvalidParameterException;
import com.llzw.apigate.OrderService;
import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.AddressBean;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.InvalidRestParameterException;
import com.llzw.apigate.service.error.RequestedDependentObjectNotFoundException;
import com.llzw.apigate.service.error.RestApiException;
import com.llzw.apigate.web.dto.OrderSearchDto;
import java.util.List;
import java.util.Optional;
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
    Optional<Product> productOptional = productRepository.findById(productId);
    if (!productOptional.isPresent()) {
      throw new RestDependentEntityNotFoundException(
          String.format("Product <%s> do not exist", productId));
    }
    Optional<Address> addressOptional = addressRepository.findById(addressId);
    if (!addressOptional.isPresent()) {
      throw new RestDependentEntityNotFoundException(
          String.format("Address <%s> do not exist", addressId));
    }
    AddressBean addressBean = addressOptional.get();
    Optional<Stock> stockOptional;
    try (Stream<Stock> validStocks =
        stockRepository
            .findByProductAndInboundedAtNotNullAndCurrentQuantityGreaterThanEqualOrderByInboundedAt(
                productOptional.get(), quantity)) {
      stockOptional = validStocks.findFirst();
    }
    if (!stockOptional.isPresent()) {
      throw new RestDependentEntityNotFoundException(
          "Cannot find an available stock specifies that quantity");
    }
    Stock stock = stockOptional.get();
    stock.decreaseCurrentQuantity(quantity);
    stockRepository.save(stock);
    Order order = new Order();
    order.setStock(stock);
    order.setAddress(addressBean);
    order.setCustomer(customer);
    order.setQuantity(quantity);
    return orderRepository.save(order);
  }

  @Override
  public List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable)
      throws RestApiException {
    try {
      // Result orders may contain other user's order, so we should filter them out.
      List<Order> allMatchingOrders =
          orderRepository
              .findAll(SearchCriterionSpecificationFactory.fromExample(example), pageable)
              .getContent();
      return allMatchingOrders.stream()
          .filter(o -> o.belongsToUser(relatedUser))
          .collect(Collectors.toList());
    } catch (IllegalAccessException e) {
      throw new RestInvalidParameterException(e.getMessage());
    }
  }

  @Override
  public Optional<Order> get(Long id) {
    return orderRepository.findById(id);
  }
}
