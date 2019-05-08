package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.AddressCreateDto;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@BasePathAwareController
@RequestMapping(value = "/addresses")
@Transactional
public class AddressController {

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  /**
   * Create a new address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PostMapping
  public ResponseEntity createAddress(@Valid AddressCreateDto dto) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Address address = new Address();
    address.setOwner(currentUser);
    address.setAddress(dto.getAddress());
    address.setProvince(dto.getProvince());
    address.setCity(dto.getCity());
    address.setDistrict(dto.getDistrict());
    address.setZip(dto.getZip());
    return RestResponseEntityFactory.success(addressRepository.save(address), HttpStatus.CREATED);
  }

  /**
   * Get a specific address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity getSpecificAddress(@PathVariable(value = "id") Long id)
      throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Address address = addressRepository.findById(id)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Address <%s> does not exist", id)));
    if (!address.belongsToUser(currentUser)) {
      throw new RestAccessDeniedException("You do not have access to this entity");
    }
    return RestResponseEntityFactory.success(address);
  }

  /**
   * Get a user's all addresses
   */
  @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
  @GetMapping
  public ResponseEntity getAddressByOwnerId(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    return RestResponseEntityFactory.success(
        addressRepository.findAllByOwner(currentUser, pageRequest)
            .collect(Collectors.toList())
    );
  }
}
