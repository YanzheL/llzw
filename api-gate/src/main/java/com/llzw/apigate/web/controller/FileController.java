package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.service.FileStorageService;
import com.llzw.apigate.web.dto.FileContainer;
import com.llzw.apigate.web.util.StandardRestResponse;
import com.llzw.apigate.web.validation.FileValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
@RestController
@BasePathAwareController
@RequestMapping(value = "/files")
public class FileController {

  @Setter(onMethod_ = @Autowired)
  private FileStorageService fileStorageService;

  @Setter(onMethod_ = @Autowired)
  private FileValidator fileValidator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(fileValidator);
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PostMapping
  public ResponseEntity upload(@Valid FileContainer fileContainer) {
//    ResourceHttpRequestHandler
    Collection<String> msgs = new ArrayList<>();
    Optional<FileMetaData> serviceResult = null;
    try {
      serviceResult = fileStorageService.save(fileContainer);
    } catch (java.io.IOException e) {
      return StandardRestResponse.errorResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return serviceResult.isPresent()
        ? StandardRestResponse.getResponseEntity(serviceResult.get(), true, HttpStatus.CREATED)
        : StandardRestResponse.getResponseEntity(msgs, false, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
