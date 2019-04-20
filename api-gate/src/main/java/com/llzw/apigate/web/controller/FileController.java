package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestInternalServerException;
import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.service.FileStorageService;
import com.llzw.apigate.web.dto.FileDto;
import com.llzw.apigate.web.validation.FileValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.Setter;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PreAuthorize("hasAuthority('OP_CREATE_FILE')")
  @PostMapping
  public ResponseEntity upload(@Valid FileDto fileDto) throws Exception {
    Collection<String> msgs = new ArrayList<>();
    Optional<FileMetaData> serviceResult = null;
    serviceResult = fileStorageService.save(fileDto);
    return serviceResult.isPresent()
        ? RestResponseEntityFactory.success(serviceResult.get(), HttpStatus.CREATED)
        : RestResponseEntityFactory
            .error(new RestInternalServerException());
  }

  @PreAuthorize("hasAuthority('OP_DELETE_FILE')")
  @DeleteMapping(value = "/{hash}")
  public ResponseEntity delete(@PathVariable(value = "hash") String hash) {
    fileStorageService.delete(hash);
    return RestResponseEntityFactory.success(null);
  }

  /**
   * I know this is ugly, but it's the only way that worked for now. There is a 'better' way using
   * <tt>ResponseEntity<Resource></tt> (see the next commented method), but it keeps throwing
   * <tt>HttpMediaTypeNotAcceptableException</tt>. No solution found on StackOverflow and Google,
   * so I gave it up.
   */
  @GetMapping(value = "/{hash}")
  public void download(@PathVariable(value = "hash") String hash, HttpServletResponse response)
      throws IOException {
    FileDto fileDto = fileStorageService.load(hash);
    Resource resource = fileDto.getFile();
    if (!fileDto.getFile().exists()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
      return;
    }
    String mimeType = fileDto.getMimeType();
    // copy it to response's OutputStream
    response.setContentType(mimeType);
    IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    response.flushBuffer();
  }

// This method is fucking not working as expected.
//  @GetMapping(value = "/{hash}")
//  public ResponseEntity<Resource> download(@PathVariable(value = "hash") String hash) {
//    try {
//      FileDto fileContainer = fileStorageService.load(hash);
//      if (!fileContainer.getFile().exists()) {
//        throw new IOException("File not found");
//      }
//      Resource containerResource = fileContainer.getFile();
//      String mimeType = fileContainer.getMimeType();
//      HttpHeaders headers = new HttpHeaders();
////      headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
//      return ResponseEntity.ok()
////          .headers(headers)
//          .contentLength(fileContainer.getFile().contentLength())
//          .contentType(MediaType.parseMediaType(mimeType))
//          .body(new InputStreamResource(containerResource.getInputStream()));
//    } catch (IOException e) {
//      return ResponseEntity.notFound().build();
////      return null;
//    }
//  }

}
