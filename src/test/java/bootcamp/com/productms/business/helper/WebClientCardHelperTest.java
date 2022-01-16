package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebClientCardHelperTest {
  @Autowired
  private WebClientCardHelper webClientCardHelper;

  private static final CardDto cardDto = new CardDto();
  private static final String id = "61dcafaa0feb3d339e4c04b2";
  private static final String productId = "61db650e31dec743727907f5";
  private static final String cardNumber = "91e91741-b1f9-4c3e-a7e3-86a899cfcb45";
  private static final String cardType = "MASTERCARD";
  private static final String cvv = "932";
  private static final int month = 1;
  private static final int year = 2027;
  private static final String status = "ACTIVE";

  @BeforeAll
  static void setUp(){
    cardDto.setId(id);
    cardDto.setProductId(productId);
    cardDto.setCardNumber(cardNumber);
    cardDto.setCardType(cardType);
    cardDto.setCvv(cvv);
    cardDto.setMonth(month);
    cardDto.setYear(year);
    cardDto.setStatus(status);

  }
  @Test
  void deleteCard() {
    Assertions.assertNotNull(webClientCardHelper.deleteCard(productId));
  }
}