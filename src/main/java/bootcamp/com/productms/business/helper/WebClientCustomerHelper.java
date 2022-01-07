package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.Customer;
import bootcamp.com.productms.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientCustomerHelper {
    @Autowired
    private WebClient webClient;

    public Mono<Customer> findCustomer(String id){
        return webClient.get()
                .uri("/api/v1/customers/" + id)
                .retrieve()
                .bodyToMono(Customer.class);
    }
    public Mono<Customer> saveCustomerWithProduct(Product product){
        return webClient.post()
                .uri("/api/v1/customers/product")
                .body(Mono.just(product),Product.class)
                .retrieve()
                .bodyToMono(Customer.class);
    }

}
