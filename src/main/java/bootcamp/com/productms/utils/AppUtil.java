package bootcamp.com.productms.utils;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.ProductCustomerDto;
import bootcamp.com.productms.model.ProductDto;
import org.springframework.beans.BeanUtils;

public class AppUtil {
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
}
