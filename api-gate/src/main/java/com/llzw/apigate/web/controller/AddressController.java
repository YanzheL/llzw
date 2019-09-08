package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.persistence.dao.AddressRepository;
import com.llzw.apigate.persistence.entity.Address;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.util.Utils;
import com.llzw.apigate.web.dto.AddressCreateDto;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
// @RestController creates duplicate endpoints with and without base-path. The same issue as described above.
@Validated
@Controller
@ResponseBody
@RequestMapping(value = "${spring.data.rest.base-path}/addresses")
@Transactional
public class AddressController {

  @Setter(onMethod_ = @Autowired)
  private AddressRepository addressRepository;

  /**
   * Create a new address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PostMapping
  public ResponseEntity createAddress(
      @Valid @RequestBody AddressCreateDto dto,
      @AuthenticationPrincipal User currentUser
  ) {
    Address address = new Address();
    address.setOwner(currentUser);
    BeanUtils.copyProperties(dto, address, Utils.getNullPropertyNames(dto));
    return RestResponseEntityFactory.success(addressRepository.save(address), HttpStatus.CREATED);
  }

  /**
   * Get a specific address
   */
  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/{id:\\d+}")
  public ResponseEntity getSpecificAddress(
      @PathVariable(value = "id") Long id,
      @AuthenticationPrincipal User currentUser
  )
      throws RestApiException {
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
  public ResponseEntity getMyAddresses(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @AuthenticationPrincipal User currentUser
  ) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
    return RestResponseEntityFactory.success(
        addressRepository.findAllByOwner(currentUser, pageRequest)
    );
  }
}
