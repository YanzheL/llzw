package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.message.error.RestRejectedByEntityException;
import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.util.Utils;
import com.llzw.apigate.web.dto.OrderSearchDto;
import com.llzw.apigate.web.dto.OrderShipDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.criteria.Predicate;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultOrderService implements OrderService {

  @Setter(onMethod_ = @Autowired)
  private OrderRepository orderRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductService productService;

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  @Setter(onMethod_ = @Autowired)
  private StockService stockService;

  @Override
  public Order create(User customer, Long productId, int quantity, Long addressId,
      String remark)
      throws RestApiException {
    Product product = productService.findById(productId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Product <%s> does not exist", productId)));
    Address address = addressRepository.findByIdAndOwner(addressId, customer)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Address <%s> do not exist or you do not have access to this entity",
                addressId)));
    List<Stock> stocks = stockService.lockStocksForProduct(product, quantity);
    if (stocks.isEmpty()) {
      throw new RestDependentEntityNotFoundException(
          "Cannot find an available stocks specifies that quantity");
    }
    Order order = new Order();
    order.setProduct(product);
    order.setStocks(stocks);
    order.setAddress(address);
    order.setCustomer(customer);
    order.setTotalAmount(product.getPrice());
    order.setQuantity(quantity);
    order.setRemark(remark);
    order.setValid(true);
    return orderRepository.save(order);
  }

  @Override
  public List<Order> search(OrderSearchDto example, User relatedUser, Pageable pageable) {
    // Result orders may contain other user's order, so we should filter them out.
    return orderRepository
        .findAll(
            makeSpec(example, relatedUser),
            pageable
        )
        .getContent()
        ;
  }

  @Override
  public Order get(String id, User relatedUser) throws RestApiException {
    return orderRepository.findByIdAndUser(UUID.fromString(id), relatedUser)
        .orElseThrow(() -> new RestEntityNotFoundException(
            String.format("Order <%s> does not exist or you do not have access to this entity", id))
        );
  }

  @Override
  public Order cancel(String id, User relatedUser) throws RestApiException {
    Order order = get(id, relatedUser);
    if (order.getShippingTime() != null) {
      throw new RestRejectedByEntityException(
          String.format("Order <%s> has already been shipped", id));
    }
    order.setValid(false);
    return orderRepository.save(order);
  }

  @Override
  public Order deliveryConfirm(String id, User relatedUser) throws RestApiException {
    Order order = get(id, relatedUser);
    if (order.isDeliveryConfirmed()) {
      throw new RestRejectedByEntityException(
          String.format("Order <%s> has already been confirmed", id));
    }
    order.setDeliveryConfirmed(true);
    return orderRepository.save(order);
  }

  @Override
  public int countOrdersAfter(Product product, Date date) {
    return orderRepository.countAllByProductAndCreatedAtAfter(product, date);
  }

  @Override
  public Order patch(String id, OrderShipDto dto, User relatedUser) throws RestApiException {
    Order order = get(id, relatedUser);
    BeanUtils.copyProperties(dto, order, Utils.getNullPropertyNames(dto));
    return orderRepository.save(order);
  }

  private Specification<Order> makeSpec(OrderSearchDto dto, User relatedUser) {
    return Order.belongsToUserSpec(relatedUser)
        .and(
            (root, criteriaQuery, criteriaBuilder) -> {
              List<Predicate> expressions = new ArrayList<>();
              if (dto.getStockId() != null) {
                expressions.add(
                    criteriaBuilder.isMember(
                        dto.getStockId(), root.get("stocks"))
                );
              }
              if (dto.getCustomerId() != null) {
                expressions.add(criteriaBuilder.equal(
                    root.get("customer").<String>get("username"), dto.getCustomerId()
                ));
              }
              if (dto.getTrackingId() != null) {
                expressions.add(criteriaBuilder.equal(
                    root.get("trackingId"), dto.getTrackingId()
                ));
              }
              if (dto.getValid() != null) {
                expressions.add(
                    criteriaBuilder.equal(
                        root.get("valid"), dto.getValid()
                    )
                );
              }
              return expressions.stream().reduce(criteriaBuilder::and).orElse(null);
            }
        )
        ;
  }

  @Override
  public Order patch(String id, OrderShipDto dto, User relatedUser) throws RestApiException {
//    Order order = get(id, relatedUser);
    Order order = orderRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new RestEntityNotFoundException(
            String.format("Order <%s> does not exist", id)));
    String seller = order.getStock().getProduct().getSeller().getUsername();
    if (!order.belongsToUser(relatedUser)) {
      throw new RestAccessDeniedException("Current user does not have access to this order");
    }
    BeanUtils.copyProperties(dto, order);
    return orderRepository.save(order);
  }
}
