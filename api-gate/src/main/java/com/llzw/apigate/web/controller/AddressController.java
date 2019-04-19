package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.dao.customquery.SearchCriterionSpecificationFactory;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.AddressDto;
import com.llzw.apigate.web.dto.AddressSearchDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
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

@RepositoryRestController
@RequestMapping(value = "/addresses")
public class AddressController {
  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

//  @Setter(onMethod_ = @Autowired)
//  private UserService userService;

  /*
  * create addresses
  * */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PostMapping(value = "")
  @Transactional
  public ResponseEntity createAddress(@Valid AddressDto addressDto) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    Address address = new Address();
    address.setOwner(currentUser);
    address.setAddress(addressDto.getAddress());
    address.setProvince(addressDto.getProvince());
    address.setCity(addressDto.getCity());
    address.setDistrict(addressDto.getDistrict());
    address.setZip(addressDto.getZip());
    Address saveOpResult = addressRepository.save(address);
    return StandardRestResponse.getResponseEntity(saveOpResult, true, HttpStatus.CREATED);
  }
  /*
  * get a specific address
  * */
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

  /*
   * get a specific address by ownerId
   * */
  @PreAuthorize("hasAnyRole('CUSTOMER')")
  @GetMapping(value = "")
  public ResponseEntity getAddressByOwnerId(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      AddressSearchDto searchDto) throws IllegalAccessException {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    List<Address> allMatchingAddresses =
        addressRepository
            .findAll(SearchCriterionSpecificationFactory.fromExample(searchDto), pageRequest)
            .getContent();
    return StandardRestResponse.getResponseEntity(allMatchingAddresses);
  }

}
