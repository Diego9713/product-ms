package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.dto.CustomerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebClientCustomerHelperTest {
  @Autowired
  private WebClientCustomerHelper webClientCustomerHelper;

  private static final CustomerDto customerDto = new CustomerDto();
  private final static String idCustomer = "61db5ffd7610bd27a53b2b8b";
  private final static String documentType = "dni";
  private final static String documentNumber = "71528107";
  private final static String customerType = "PERSONAL";
  private final static String firstName = "Diego";
  private final static String lastName = "Tafur";
  private final static String address = "limatambo norte";
  private final static String references = "videna";
  private final static String phoneNumber = "9492507508";
  private final static String civilStatus = "soltero";
  private final static String email = "tafur232@gmail.com";
  private final static String status = "ACTIVE";
  private final static boolean owner = true;

  @BeforeAll
  static void setUp(){
    customerDto.setId(idCustomer);
    customerDto.setDocumentType(documentType);
    customerDto.setDocumentNumber(documentNumber);
    customerDto.setCustomerType(customerType);
    customerDto.setFirstName(firstName);
    customerDto.setLastName(lastName);
    customerDto.setAddress(address);
    customerDto.setReferences(references);
    customerDto.setPhoneNumber(phoneNumber);
    customerDto.setCivilStatus(civilStatus);
    customerDto.setEmail(email);
    customerDto.setOwner(owner);
    customerDto.setStatus(status);

    customerDto.getId();
    customerDto.getDocumentType();
    customerDto.getDocumentNumber();
    customerDto.getCustomerType();
    customerDto.getFirstName();
    customerDto.getLastName();
    customerDto.getAddress();
    customerDto.getReferences();
    customerDto.getPhoneNumber();
    customerDto.getCivilStatus();
    customerDto.getEmail();
    customerDto.getStatus();
  }
  @Test
  void findCustomer() {
    Assertions.assertNotNull(webClientCustomerHelper.findCustomer(idCustomer));
  }

  @Test
  void findCustomerByDni() {
    Assertions.assertNotNull(webClientCustomerHelper.findCustomerByDni(documentNumber));
  }
}