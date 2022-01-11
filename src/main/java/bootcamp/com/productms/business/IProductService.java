package bootcamp.com.productms.business;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.ProductCustomerDto;
import bootcamp.com.productms.model.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {

    Flux<ProductDto> findAllProduct();

    Mono<Product> findByIdProduct(String id);

    Flux<ProductDto> findByProductNumber(String id);

    Flux<ProductCustomerDto> findProductByCustomer(String customerId);

    Mono<ProductDto> createProduct(Product product);

    Mono<ProductDto> registerProductToCustomer(String accountNumber, String dni);

    Mono<Product> updateProduct(Product product, String id);

    Mono<ProductDto> removeProduct(String id);
}
