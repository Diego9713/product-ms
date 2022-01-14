package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientCustomerHelper {
  @Autowired
  private WebClient webClient;

  /**
   * Method to search for a customer by id.
   *
   * @param id -> customer identifier.
   * @return object customer find.
   */
  public Mono<CustomerDto> findCustomer(String id) {
    return webClient.get()
      .uri("/api/v1/customers/" + id)
      .retrieve()
      .bodyToMono(CustomerDto.class);
  }

  /**
   * Method to search for a customer by dni.
   *
   * @param dni -> customer document number.
   * @return object customer find.
   */
  public Mono<CustomerDto> findCustomerByDni(String dni) {
    return webClient.get()
      .uri("/api/v1/customers/documentnumber/" + dni)
      .retrieve()
      .bodyToMono(CustomerDto.class);
  }

}
