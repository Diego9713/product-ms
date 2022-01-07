package bootcamp.com.productms.business;

import bootcamp.com.productms.model.Customer;
import bootcamp.com.productms.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    Flux<Product> findAllProduct();

    Mono<Product> findByIdProduct(String id);

    Mono<Customer> createProduct(Product product);

    Mono<Product> updateProduct(Product product, String id);

    Mono<Product> removeProduct(String id);
}
