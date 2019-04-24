package com.llzw.apigate.service;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.ProductCreateDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SimpleProductService implements ProductService {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private FileStorageService fileStorageService;

  @Value("${spring.data.rest.base-path}")
  private String apiBasePath;

  @Override
  public boolean updateValid(Long id, User seller) throws RestApiException {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Product <%s> does not exist", id)));
    if (!product.belongsToSeller(seller)) {
      throw new RestAccessDeniedException("You do not have access to this entity");
    }
    product.setValid(false);
    productRepository.save(product);
    return true;
  }

  @Override
  public Product create(ProductCreateDto dto, User seller) throws RestApiException {
    if (!fileStorageService.increaseReferenceCount(dto.getCaFile())) {
      throw new RestEntityNotFoundException(
          String.format("CA file <%s> does not exist", dto.getCaFile()));
    }
    String introduction = dto.getIntroduction();
    List<String> paths = searchFilePaths(introduction);
    for (String path : paths) {
      if (!fileStorageService.increaseReferenceCount(path)) {
        throw new RestEntityNotFoundException(
            String.format("Referenced file <%s> does not exist", path));
      }
    }
    Product product = new Product();
    product.setSeller(seller);
    product.setName(dto.getName());
    product.setIntroduction(dto.getIntroduction());
    product.setPrice(dto.getPrice());
    product.setCaId(dto.getCaId());
    product.setValid(true);
    product.setCaFile(dto.getCaFile());
    return productRepository.save(product);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public List<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable).getContent();
  }

  /**
   * Search for file reference links used in product's introduction body. It will only detect files
   * whose link is similar to http://xxx.com/api/v2/files/{HASH}
   *
   * @return File hash paths.
   */
  private List<String> searchFilePaths(String body) {
    Pattern pattern = Pattern.compile(apiBasePath + "/files/([a-z0-9]{64})");
    UrlDetector parser = new UrlDetector(body, UrlDetectorOptions.Default);
    List<Url> found = parser.detect();
    List<String> paths = new ArrayList<>();
    for (Url url : found) {
      String path = url.getPath();
      Matcher matcher = pattern.matcher(path);
      if (matcher.matches()) {
        paths.add(matcher.group(1));
      }
    }
    return paths;
  }
}
