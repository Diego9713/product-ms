package bootcamp.com.productms.utils;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import org.springframework.beans.BeanUtils;

public class AppUtil {

  private AppUtil() {
  }

  /**
   * Method to modify the return of data.
   *
   * @param product -> product object with entered data.
   * @return object modified.
   */
  public static ProductDto entityToProductDto(Product product) {
    ProductDto productDto = new ProductDto();
    BeanUtils.copyProperties(product, productDto);
    return productDto;
  }

  /**
   * Method to modify the return of data.
   *
   * @param productDto -> product object with entered data.
   * @return object modified.
   */
  public static Product productDtoToEntity(ProductDto productDto) {
    Product product = new Product();
    BeanUtils.copyProperties(productDto, product);
    return product;
  }

  /**
   * Method to modify the return of the product with respect to the customer.
   *
   * @param product -> product object with entered data.
   * @return object modified.
   */
  public static ProductCustomerDto entityToProductCustomerDto(Product product) {
    ProductCustomerDto productCustomerDto = new ProductCustomerDto();
    BeanUtils.copyProperties(product, productCustomerDto);
    return productCustomerDto;
  }

  /**
   * Method that returns average daily salary object.
   *
   * @param product -> product object with entered data.
   * @return object modified.
   */
  public static ProductSpdDto entityToProductSpdDto(Product product) {
    ProductSpdDto productSpdDto = new ProductSpdDto();
    BeanUtils.copyProperties(product, productSpdDto);
    return productSpdDto;
  }
}
