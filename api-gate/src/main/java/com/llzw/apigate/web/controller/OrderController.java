package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriteriaSpecificationFactory;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterion;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.Stock;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.OrderDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RepositoryRestController
@RestController
@BasePathAwareController
@RequestMapping(value = "/orders")
public class OrderController {

  @Setter(onMethod_ = @Autowired)
  private OrderRepository orderRepository;

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  @Setter(onMethod_ = @Autowired)
  private StockRepository stockRepository;

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "")
  public ResponseEntity searchOrders(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @RequestParam(value = "customerId", required = false) String customerId,
      @RequestParam(value = "addressId", required = false) Long addressId,
      @RequestParam(value = "stockId", required = false) Long stockId,
      @RequestParam(value = "trackingId", required = false) String trackingId) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    // Result orders may contain other user's order, so we should filter them out.
    List<Order> allMatchingOrders =
        orderRepository
            .findAll(findByExample(customerId, addressId, stockId, trackingId), pageRequest)
            .getContent();
    List<Order> res =
        allMatchingOrders.stream()
            .filter(o -> o.belongsToUser(currentUser))
            .collect(Collectors.toList());
    return StandardRestResponse.getResponseEntity(res);
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/{id}")
  public ResponseEntity getOrder(@PathVariable(value = "id") Long id) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Optional<Order> res = orderRepository.findById(id);
    if (!res.isPresent()) {
      return StandardRestResponse.getResponseEntity(null, false, HttpStatus.NOT_FOUND);
    }
    Order order = res.get();
    return order.belongsToUser(currentUser)
        ? StandardRestResponse.getResponseEntity(order)
        : StandardRestResponse
            .getResponseEntity("Current user does not have access to this order", false,
                HttpStatus.FORBIDDEN);
  }

  @PreAuthorize("hasAuthority('OP_CREATE_ORDER')")
  @PostMapping(value = "")
  @Transactional
  public ResponseEntity createOrder(@Valid OrderDto orderDto) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Optional<Product> productOptional = productRepository.findById(orderDto.getProductId());
    if (!productOptional.isPresent()) {
      return StandardRestResponse.getResponseEntity(
          "Cannot find specified product", false, HttpStatus.NOT_FOUND);
    }
    Optional<Address> addressOptional = addressRepository.findById(orderDto.getAddressId());
    if (!addressOptional.isPresent()) {
      return StandardRestResponse.getResponseEntity(
          "Cannot find specified address", false, HttpStatus.NOT_FOUND);
    }
    Optional<Stock> stockOptional;
    try (Stream<Stock> validStocks =
        stockRepository
            .findByProductIdAndInboundedAtNotNullAndCurrentQuantityGreaterThanEqualOrderByInboundedAt(
                productOptional.get(), orderDto.getQuantity())) {
      stockOptional = validStocks.findFirst();
    }
    if (!stockOptional.isPresent()) {
      return StandardRestResponse.getResponseEntity(
          "Cannot find an available stock specifies that quantity", false, HttpStatus.NOT_FOUND);
    }

    Order order = new Order();
    order.setStock(stockOptional.get());
    order.setAddress(addressOptional.get());
    order.setCustomer(currentUser);
    order.setQuantity(orderDto.getQuantity());
    Order saveOpResult = orderRepository.save(order);
    return StandardRestResponse.getResponseEntity(saveOpResult, true, HttpStatus.CREATED);
  }

  // TODO: It may lead to SQL injection.
  private Specification<Order> findByExample(
      String customerId, Long addressId, Long stockId, String trackingId) {
    List<SearchCriterion> criteria = new ArrayList<>();
    if (customerId != null) {
      criteria.add(new SearchCriterion("customerId", "=", customerId));
    }
    if (addressId != null) {
      criteria.add(new SearchCriterion("addressId", "=", addressId));
    }
    if (stockId != null) {
      criteria.add(new SearchCriterion("stockId", "=", stockId));
    }
    if (trackingId != null) {
      criteria.add(new SearchCriterion("trackingId", "=", trackingId));
    }
    return SearchCriteriaSpecificationFactory.and(criteria);
  }
}
