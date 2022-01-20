package bootcamp.com.productms.business.impl;

import static org.mockito.Mockito.when;

import bootcamp.com.productms.business.helper.FilterProductHelper;
import bootcamp.com.productms.business.helper.WebClientCardHelper;
import bootcamp.com.productms.business.helper.WebClientCustomerHelper;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.CustomerDto;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import bootcamp.com.productms.repository.IProductRepository;
import com.google.gson.Gson;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
  @MockBean
  private IProductRepository productRepository;
  @MockBean
  private WebClientCustomerHelper webClientCustomerHelper;
  @MockBean
  private WebClientCardHelper webClientCardHelper;
  @MockBean
  private FilterProductHelper filterProductHelper;
  @Autowired
  private ProductService productService;

  public static MockWebServer mockBackEnd;
  private static final Product product = new Product();
  private static final ProductDto productDto = new ProductDto();
  private static final List<Product> productDtoList = new ArrayList<>();
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
  private static final LocalDateTime transactNumberDay = null;
  private static final double creditLimit = 0;
  private static final String customer = "61db5ffd7610bd27a53b2b8b";
  private static final String status = "ACTIVE";
  private static final LocalDateTime createdAt = LocalDateTime.now();
  private static final String createdBy = "pedro";
  private static final LocalDate updateAt = LocalDate.now();
  private static final LocalDate expiredDate = LocalDate.parse("2023-01-19");
  private static final String updateBy = "pedro";
  private static final double minimumAverageAmount = 0;
  private static final double averageDailyBalance = 0;
  private static final LocalDate averageDailyBalanceDay = LocalDate.now();
                              /** Customer**/
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
  private final static boolean owner = true;

  @BeforeAll
  static void setUp(@Value("${server.port}") int port) throws IOException {
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
                        /**Customer**/
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
    BeanUtils.copyProperties(productDto, product);
    BeanUtils.copyProperties(productDto, productCustomerDto);
    BeanUtils.copyProperties(productDto, productSpdDto);
    productDtoList.add(product);
    mockBackEnd = new MockWebServer();
    mockBackEnd.start(port);

  }

  @AfterAll
  static void tearDown() throws IOException {
    mockBackEnd.shutdown();
  }

  @BeforeEach
  void setUp() {
    Gson gson = new Gson();
    mockBackEnd.url("http://localhost:9090/customer");
    mockBackEnd.enqueue(new MockResponse()
      .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .setBody(gson.toJson(customerDto))
      .setResponseCode(HttpStatus.OK.value()));
  }

  @Test
  void findAllProduct() {
    when(productRepository.findAll()).thenReturn(Flux.just(product));
    Assertions.assertNotNull(productService.findAllProduct());
  }

  @Test
  void findByIdProduct() {
    when(productRepository.findById(id)).thenReturn(Mono.just(product));
    Assertions.assertNotNull(productService.findByIdProduct(id));
  }

  @Test
  void findByProductNumber() {
    when(productRepository.findByAccountNumber(accountNumber)).thenReturn(Flux.just(product));
    Assertions.assertNotNull(productService.findByProductNumber(accountNumber));
  }

  @Test
  void findProductByCustomer() {
    when(productRepository.findByCustomer(customer)).thenReturn(Flux.just(product));
    Assertions.assertNotNull(productService.findProductByCustomer(customer));
  }

  @Test
  void findAverageDailyBalance() {
    when(productRepository.findByCustomerAndAverageDailyBalanceDay(customer, averageDailyBalanceDay)).thenReturn(Flux.just(product));
    Assertions.assertNotNull(productService.findAverageDailyBalance(customer, averageDailyBalanceDay.toString()));
  }

  @Test
  void generateSpd() {
    when(productRepository.findById(id)).thenReturn(Mono.just(product));
    Assertions.assertNotNull(productService.generateSpd(id));
  }

  @Test
  void createProduct() {
    when(webClientCustomerHelper.findCustomer(customer)).thenReturn(Mono.just(customerDto));
    when(productRepository.findByCustomer(customer)).thenReturn(Flux.just(product));
    when(productRepository.save(product)).thenReturn(Mono.just(product));
    when(filterProductHelper.createObjectProduct(productDto,customerDto,productDtoList)).thenReturn(productDto);
    when(filterProductHelper.isSave(customerDto,productDto,productDtoList)).thenReturn(Boolean.TRUE);
    Assertions.assertNotNull(productService.createProduct(productDto));
  }

  @Test
  void registerProductToCustomer() {
    when(productRepository.findBySubAccountNumber(subAccountNumber)).thenReturn(Flux.just(product));
    when(webClientCustomerHelper.findCustomerByDni(documentNumber)).thenReturn(Mono.just(customerDto));
    when(productRepository.save(product)).thenReturn(Mono.just(product));
    when(filterProductHelper.filterProductToCustomer(customerDto)).thenReturn(Mono.just(Boolean.TRUE));
    when(filterProductHelper.searchProductRegister(Mono.just(customerDto),Mono.just(productDtoList))).thenReturn(Mono.just(Boolean.TRUE));
    Assertions.assertNotNull(productService.registerProductToCustomer(accountNumber,documentNumber));
  }

  @Test
  void updateProduct() {
    when(webClientCustomerHelper.findCustomer(customer)).thenReturn(Mono.just(customerDto));
    when(productRepository.save(product)).thenReturn(Mono.just(product));
    when(productRepository.findById(id)).thenReturn(Mono.just(product));
    when(filterProductHelper.updateObjectProduct(productDto,product,customerDto)).thenReturn(Mono.just(productDto));
    Assertions.assertNotNull(productService.updateProduct(productDto,id));
  }

  @Test
  void removeProduct() {
    when(productRepository.findById(id)).thenReturn(Mono.just(product));
    when(webClientCardHelper.deleteCard(id)).thenReturn(Mono.just(Boolean.TRUE));
    when(productRepository.save(product)).thenReturn(Mono.just(product));
    Assertions.assertNotNull(productService.removeProduct(id));
  }
}