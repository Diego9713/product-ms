package bootcamp.com.productms.utils;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.ProductCustomerDto;
import bootcamp.com.productms.model.ProductDto;
import org.springframework.beans.BeanUtils;

public class AppUtil {

    public static ProductDto entityToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product,productDto);
        return productDto;
    }
    public static ProductCustomerDto entityToProductCustomerDto(Product product) {
        ProductCustomerDto productCustomerDto = new ProductCustomerDto();
        BeanUtils.copyProperties(product,productCustomerDto);
        return productCustomerDto;
    }
}
