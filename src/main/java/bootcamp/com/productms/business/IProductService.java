package bootcamp.com.productms.business;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {

  Flux<ProductDto> findAllProduct();

  Mono<Product> findByIdProduct(String id);

  Flux<ProductSpdDto> findAverageDailyBalance(String id, String dateTime);

  Mono<ProductDto> generateSpd(String id);

  Flux<ProductDto> findByProductNumber(String id);

  Flux<ProductCustomerDto> findProductByCustomer(String customerId);

  Mono<ProductDto> createProduct(ProductDto product);

  Mono<ProductDto> registerProductToCustomer(String accountNumber, String dni);

  Mono<ProductDto> updateProduct(ProductDto product, String id);

  Mono<ProductDto> removeProduct(String id);
}
