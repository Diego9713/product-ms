package bootcamp.com.productms.repository;

import bootcamp.com.productms.model.Product;
import java.time.LocalDate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IProductRepository extends ReactiveMongoRepository<Product, String> {
  Flux<Product> findByCustomer(String customer);

  Flux<Product> findByCustomerAndAverageDailyBalanceDay(String customer, LocalDate dateTime);

  Flux<Product> findByAccountTypeAndCreatedAtBetween(String accountType, LocalDate from, LocalDate until);

  Flux<Product> findByAccountNumber(String accountNumber);

  Flux<Product> findBySubAccountNumber(String subAccountNumber);
}
