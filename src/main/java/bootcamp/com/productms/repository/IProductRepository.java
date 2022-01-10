package bootcamp.com.productms.repository;

import bootcamp.com.productms.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IProductRepository extends ReactiveMongoRepository<Product,String> {
    Flux<Product> findByCustomer(String customer);
    Flux<Product> findByAccountNumber(String accountNumber);
}
