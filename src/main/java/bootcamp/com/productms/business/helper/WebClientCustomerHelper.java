package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientCustomerHelper {
    @Autowired
    private WebClient webClient;

    public Mono<CustomerDto> findCustomer(String id){
        return webClient.get()
                .uri("/api/v1/customers/" + id)
                .retrieve()
                .bodyToMono(CustomerDto.class);
    }
    public Mono<CustomerDto> findCustomerByDni(String dni){
        return webClient.get()
                .uri("/api/v1/customers/document_number/" + dni)
                .retrieve()
                .bodyToMono(CustomerDto.class);
    }

}
