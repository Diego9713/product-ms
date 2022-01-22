package bootcamp.com.productms.expose;

import static org.mockito.Mockito.when;

import bootcamp.com.productms.business.impl.ProductService;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "20000")
class ProductControllerTest {
  @MockBean
  private ProductService productService;
  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private ProductController productController;

  private static final Product product = new Product();
  private static final ProductDto productDto = new ProductDto();
  private static final ProductCustomerDto productCustomerDto = new ProductCustomerDto();
  private static final ProductSpdDto productSpdDto = new ProductSpdDto();
  private static final String id = "61db64d731dec743727907f3";
  private static final String accountType = "SAVING";
  private static final String accountNumber = "d558f2fb-dc37-4b32-ba9f-88b31d8efe10";
  private static final String subAccountNumber = "d558f2fb-dc37-4b32-ba9f-88b31d8efe10";
  private static final int level = 1;
  private static final String currency = "PEN";
  private static final double amount = 6300;
  private static final double maintenanceCommission = 0;
  private static final LocalDateTime maintenanceCommissionDay = null;
  private static final int maxTransactNumber = 10;
  private static final LocalDate transactNumberDay = null;
  private static final double creditLimit = 0;
  private static final String customer = "61db5ffd7610bd27a53b2b8b";
  private static final String status = "ACTIVE";
  private static final LocalDate createdAt = LocalDate.now();
  private static final String createdBy = "pedro";
  private static final LocalDate updateAt = LocalDate.now();
  private static final LocalDate expiredDate = LocalDate.parse("2023-01-19");
  private static final String updateBy = "pedro";
  private static final double minimumAverageAmount = 0;
  private static final double averageDailyBalance = 0;
  private static final LocalDate averageDailyBalanceDay = LocalDate.now();

  @BeforeAll
  static void setUp() {
    productDto.setId(id);
    productDto.setAccountType(accountType);
    productDto.setAccountNumber(accountNumber);
    productDto.setCurrency(currency);
    productDto.setAmount(amount);
    productDto.setMaintenanceCommission(maintenanceCommission);
    productDto.setMaintenanceCommissionDay(maintenanceCommissionDay);
    productDto.setMaxTransactNumber(maxTransactNumber);
    productDto.setTransactNumberDay(transactNumberDay);
    productDto.setCreditLimit(creditLimit);
    productDto.setCustomer(customer);
    productDto.setStatus(status);
    productDto.setCreatedAt(createdAt);
    productDto.setCreatedBy(createdBy);
    productDto.setUpdateAt(updateAt);
    productDto.setUpdateBy(updateBy);
    productDto.setMinimumAverageAmount(minimumAverageAmount);
    productDto.setAverageDailyBalance(averageDailyBalance);
    productDto.setAverageDailyBalanceDay(averageDailyBalanceDay);
    productDto.setSubAccountNumber(subAccountNumber);
    productDto.setLevel(level);
    productDto.setExpiredDate(expiredDate);
    BeanUtils.copyProperties(productDto, product);
    BeanUtils.copyProperties(productDto, productCustomerDto);
    BeanUtils.copyProperties(productDto, productSpdDto);
  }

  @Test
  @DisplayName("GET -> /api/v1/products")
  void findAllProduct() {
    when(productService.findAllProduct()).thenReturn(Flux.just(productDto));

    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products")
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/products/accountnumber/{account}")
  void findOneProductNumber() {
    when(productService.findByProductNumber(accountNumber)).thenReturn(Flux.just(productDto));
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products/accountnumber/" + accountNumber)
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/products/customer/{customer}")
  void findProductByCustomer() {
    when(productService.findProductByCustomer(customer)).thenReturn(Flux.just(productCustomerDto));
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products/customer/" + customer)
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/products/averagedailydalance/{customerId}")
  void findAverageDailyBalance() {
    when(productService.findAverageDailyBalance(customer, "2022-01-15")).thenReturn(Flux.just(productSpdDto));
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products/averagedailydalance/" + customer + "?date=2022-01-15")
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/products/accounttype/{accounttype}")
  void findByAccountTypeAndCreatedAtBetween() {
    when(productService.findByAccountTypeAndCreatedAtBetween(accountType, "2022-01-12", "2022-01-19"))
      .thenReturn(Flux.just(productDto));
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products/accounttype/" + accountType + "?from=2022-01-12&until=2022-01-19")
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/products/{id}")
  void findOneProduct() {
    when(productService.findByIdProduct(id)).thenReturn(Mono.just(product));
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
      .uri("/api/v1/products/" + id)
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
    responseSpec.expectBody()
      .jsonPath("$.accountType").isEqualTo(productDto.getAccountType());
  }

  @Test
  @DisplayName("POST -> /api/v1/products")
  void saveProduct() {
    when(productService.createProduct(productDto)).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productController.saveProduct(productDto));
  }

  @Test
  @DisplayName("POST -> /api/v1/products/registercustomer")
  void registerProductToCustomer() {
    when(productService.registerProductToCustomer(subAccountNumber, "1254875")).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productController.registerProductToCustomer(accountNumber, "1254875"));
  }

  @Test
  @DisplayName("PUT -> /api/v1/products/{id}")
  void updateProduct() {
    when(productService.updateProduct(productDto, id)).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productController.updateProduct(id, productDto));
  }

  @Test
  @DisplayName("PUT -> /api/v1/products/generate/{productId}")
  void generateSpd() {
    when(productService.generateSpd(id)).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productController.generateSpd(id));
  }

  @Test
  @DisplayName("DELETE -> /api/v1/products/{id}")
  void removeProduct() {
    when(productService.removeProduct(id)).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productController.removeProduct(id));
  }

  @Test
  void fallBackPostProduct() {
    Assertions.assertNotNull(productController.fallBackPostProduct(productDto, new RuntimeException("")));
  }

  @Test
  void fallBackPostProductRegisterCustomer() {
    Assertions.assertNotNull(productController.fallBackPostProductRegisterCustomer(accountNumber, "1254875", new RuntimeException("")));
  }

  @Test
  void fallBackPutProduct() {
    Assertions.assertNotNull(productController.fallBackPutProduct(id, productDto, new RuntimeException("")));
  }

  @Test
  void fallBackDeleteProduct() {
    Assertions.assertNotNull(productController.fallBackDeleteProduct(id, new RuntimeException("")));
  }
}