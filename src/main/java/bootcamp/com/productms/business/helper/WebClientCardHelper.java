package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.dto.CardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientCardHelper {
  @Autowired
  private WebClient webClient;

  /**
   * Method to change the status of a card.
   *
   * @param productId -> customer identifier.
   * @return a condition if the card is status changed.
   */
  public Mono<Boolean> deleteCard(String productId) {
    Mono<CardDto> cardDtoMono = webClient.delete()
        .uri("/api/v1/cards/product/" + productId)
        .retrieve()
        .bodyToMono(CardDto.class);
    return cardDtoMono.flatMap(cardDto -> cardDto.getId() != null ? Mono.just(true) : Mono.just(false));

  }

}
