package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.AddressCreateDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasePathAwareController
@RequestMapping(value = "/addresses")
public class AddressController {

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  /**
   * Create a new address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PostMapping(value = "")
  @Transactional
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
    Address saveOpResult = addressRepository.save(address);
    return StandardRestResponse.getResponseEntity(saveOpResult, true, HttpStatus.CREATED);
  }

  /**
   * Get a specific address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/{id}")
  public ResponseEntity getSpecificAddress(@PathVariable(value = "id") Long id) {
    Optional<Address> res = addressRepository.findById(id);
    if (!res.isPresent()) {
      return StandardRestResponse.getResponseEntity(null, false, HttpStatus.NOT_FOUND);
    }
    Address address = res.get();
    return StandardRestResponse.getResponseEntity(address, true);
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
    return StandardRestResponse.getResponseEntity(
        addressRepository.findAllByOwner(currentUser, pageRequest)
            .collect(Collectors.toList())
    );
  }
}
