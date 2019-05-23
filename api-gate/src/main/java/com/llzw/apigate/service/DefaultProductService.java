package com.llzw.apigate.service;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestEntityNotFoundException;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.entity.Product;
import com.llzw.apigate.persistence.entity.ProductStat;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.util.Utils;
import com.llzw.apigate.web.dto.ProductCreateDto;
import com.llzw.apigate.web.dto.ProductSearchDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultProductService implements ProductService {

  @Setter(onMethod_ = @Autowired)
  private ProductRepository productRepository;

  @Setter(onMethod_ = @Autowired)
  private FileStorageService fileStorageService;

  @Setter(onMethod_ = @Autowired)
  protected ProductStatisticsService productStatisticsService;

  @Value("${spring.data.rest.base-path}")
  private String apiBasePath;

  @Override
  public boolean invalidate(Long id, User seller) throws RestApiException {
    Product product = productRepository.findByIdAndSeller(id, seller)
        .orElseThrow(
            () -> new RestEntityNotFoundException(
                String
                    .format("Product <%s> does not exist or you do not have access to this entity",
                        id)
            )
        );
    product.setValid(false);
    productRepository.save(product);
    return true;
  }

  @Override
  public Product create(ProductCreateDto dto, User seller) throws RestApiException {
    fileStorageService.increaseReferenceCount(dto.getCaFile());
    String introduction = dto.getIntroduction();
    List<String> paths = searchFilePaths(introduction);
    for (String path : paths) {
      fileStorageService.increaseReferenceCount(path);
    }
    Product product = new Product();
    product.setSeller(seller);
    product.setValid(true);
    BeanUtils.copyProperties(dto, product, Utils.getNullPropertyNames(dto));
    product.setStat(new ProductStat());
    List<String> mainImageFiles = dto.getMainImageFiles();
    if (mainImageFiles != null) {
      mainImageFiles.stream()
          .filter(fileStorageService::isAcceptablePath)
          .forEach(fileStorageService::increaseReferenceCount);
    }
    return productRepository.save(product);
  }

  @Override
  public Product update(ProductCreateDto dto, Long id, User seller) throws RestApiException {
    Product product = productRepository.findByIdAndSeller(id, seller)
        .orElseThrow(
            () -> new RestEntityNotFoundException(
                String
                    .format("Product <%s> does not exist or you do not have access to this entity",
                        id)
            )
        );
    List<String> oldMainImageFiles = product.getMainImageFiles();
    List<String> newMainImageFiles = dto.getMainImageFiles();
    oldMainImageFiles.stream().filter(o -> !newMainImageFiles.contains(o))
        .forEach(fileStorageService::delete);
    newMainImageFiles.stream().filter(o -> !oldMainImageFiles.contains(o))
        .forEach(fileStorageService::increaseReferenceCount);
    BeanUtils.copyProperties(dto, product, Utils.getNullPropertyNames(dto));
    return productRepository.save(product);
  }

  @Override
  public Optional<Product> findById(Long id) {
    Optional<Product> product = productRepository.findById(id);
    product.ifPresent(productStatisticsService::updateStat);
    return product;
  }

  @Override
  public List<Product> search(Pageable pageable, ProductSearchDto dto) throws RestApiException {
    String nameQueryString = dto.getName();
    String introductionQueryString = dto.getName();
    String global = dto.getGlobal();
    List<Product> result;
    if (global != null) {
      result = productRepository.searchByNameOrIntroductionWithCustomQuery(global);
      if (!dto.isValid()) {
        result = result.stream().filter(Product::isValid).collect(Collectors.toList());
      }
    } else if (nameQueryString != null || introductionQueryString != null) {
      Product example = new Product();
      BeanUtils.copyProperties(dto, example, Utils.getNullPropertyNames(dto));
      result = productRepository.searchByExample(example);
    } else {
      result = productRepository.findAllByValid(dto.isValid());
    }
    result.forEach(productStatisticsService::updateStat);
    return result;
  }

  /**
   * Search for file reference links used in product's introduction body. It will only detect files
   * whose link is similar to http://xxx.com/api/v2/files/{HASH}
   *
   * @return File hash paths.
   */
  private List<String> searchFilePaths(String body) {
    Pattern pattern = Pattern.compile(apiBasePath + "/files/([a-z0-9]{64})");
    try {
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
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  @Override
  public Product save(Product product) {
    return productRepository.save(product);
  }
}
