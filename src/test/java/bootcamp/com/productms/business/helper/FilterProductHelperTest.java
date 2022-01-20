package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.CustomerDto;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import bootcamp.com.productms.utils.CommonConstants;
import bootcamp.com.productms.utils.ConstantsCustomerBusiness;
import bootcamp.com.productms.utils.ConstantsCustomerPersonal;
import bootcamp.com.productms.utils.ConstantsPersonal;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilterProductHelperTest {
  @Autowired
  private FilterProductHelper filterProductHelper;

  private static final Product product = new Product();
  private static final ProductDto productDto = new ProductDto();
  private static final List<Product> productDtoList = new ArrayList<>();
  private static final ProductCustomerDto productCustomerDto = new ProductCustomerDto();
  private static final ProductSpdDto productSpdDto = new ProductSpdDto();
  private static final String id = "61db64d731dec743727907f3";
  private static final String accountType = "SAVING";
  private static final String accountNumber = "d85c241a-2eb7-40da-938c-097f30d3756f";
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
  private static final String updateBy = "pedro";
  private static final double minimumAverageAmount = 0;
  private static final double averageDailyBalance = 0;
  private static final LocalDate averageDailyBalanceDay = LocalDate.now();
  /**
   * Customer
   **/
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
  }

  @Test
  void createObjectProduct() {
    //Assertions.assertNotNull(filterProductHelper.createObjectProduct(productDto,customerDto));
  }

  @Test
  void updateObjectProduct() {
    Assertions.assertNotNull(filterProductHelper.updateObjectProduct(productDto,product,customerDto));
  }

  @Test
  void filterProduct() {
    productDto.setAccountType(CommonConstants.SAVING.name());
    Assertions.assertNotNull(filterProductHelper.filterProduct(productDto,customerDto));
    productDto.setAccountType(CommonConstants.CURRENT.name());
    Assertions.assertNotNull(filterProductHelper.filterProduct(productDto,customerDto));
    productDto.setAccountType(CommonConstants.FIXED_TERM.name());
    Assertions.assertNotNull(filterProductHelper.filterProduct(productDto,customerDto));
    productDto.setAccountType(CommonConstants.CREDIT.name());
    Assertions.assertNotNull(filterProductHelper.filterProduct(productDto,customerDto));
  }

  @Test
  void isSave() {
    Assertions.assertTrue(filterProductHelper.isSave(customerDto,productDto,productDtoList));
    customerDto.setCustomerType(ConstantsCustomerBusiness.BUSINESS.name());
    productDto.setAccountType(CommonConstants.CURRENT.name());
    product.setAccountType(CommonConstants.CURRENT.name());
    List<Product> productList1 = new ArrayList<>();
    productList1.add(product);
    Assertions.assertTrue(filterProductHelper.isSave(customerDto,productDto,productList1));
    customerDto.setCustomerType(ConstantsCustomerBusiness.BUSINESS_PYME.name());
    product.setAccountType(CommonConstants.CREDIT.name());
    productDto.setAccountType(CommonConstants.CURRENT.name());
    List<Product> productList2 = new ArrayList<>();
    productList2.add(product);
    Assertions.assertTrue(filterProductHelper.isSave(customerDto,productDto,productList2));

  }

  @Test
  void isAccountTypePersonal() {
    productDto.setAccountType(CommonConstants.CURRENT.name());
    Assertions.assertTrue(filterProductHelper.isAccountTypePersonal(productDto,customerDto,productDtoList));
    customerDto.setCustomerType(ConstantsCustomerPersonal.PERSONAL_VIP.name());
    product.setAccountType(CommonConstants.CREDIT.name());
    List<Product> productList = new ArrayList<>();
    productList.add(product);
    Assertions.assertTrue(filterProductHelper.isAccountTypePersonal(productDto,customerDto,productList));
  }

  @Test
  void isAccountTypeBusiness() {
    customerDto.setCustomerType(ConstantsCustomerBusiness.BUSINESS.name());
    productDto.setAccountType(CommonConstants.CURRENT.name());
    product.setAccountType(CommonConstants.CURRENT.name());
    List<Product> productList1 = new ArrayList<>();
    productList1.add(product);
    Assertions.assertTrue(filterProductHelper.isAccountTypeBusiness(productDto,customerDto,productList1));
    customerDto.setCustomerType(ConstantsCustomerBusiness.BUSINESS_PYME.name());
    product.setAccountType(CommonConstants.CREDIT.name());
    productDto.setAccountType(CommonConstants.CURRENT.name());
    List<Product> productList2 = new ArrayList<>();
    productList2.add(product);
    Assertions.assertTrue(filterProductHelper.isAccountTypeBusiness(productDto,customerDto,productList2));
  }

  @Test
  void filterAccountType() {
    product.setAccountType(CommonConstants.CREDIT.name());
    List<Product> productList = new ArrayList<>();
    productList.add(product);
    Assertions.assertTrue(filterProductHelper.filterAccountType(productList));
  }

  @Test
  void isPermissionPersonal() {
    Assertions.assertTrue(filterProductHelper.isPermissionPersonal(productDtoList,productDto));
  }

  @Test
  void isPermissionBusiness() {
   product.setAccountType(CommonConstants.CREDIT.name());
    List<Product> productList = new ArrayList<>();
    productList.add(product);
    Assertions.assertTrue(filterProductHelper.isPermissionBusiness(productList));
  }

  @Test
  void filterPermissionPersonal() {
    productDto.setAccountType(CommonConstants.FIXED_TERM.name());
    Assertions.assertTrue(filterProductHelper.filterPermissionPersonal(productDtoList,productDto));
  }

  @Test
  void filterPermissionBusiness() {
    productDto.setAccountType(ConstantsPersonal.FIXED_TERM.name());
    product.setAccountType(CommonConstants.CREDIT.name());
    List<Product> productList = new ArrayList<>();
    productList.add(product);
    Assertions.assertTrue(filterProductHelper.filterPermissionBusiness(productList,productDto));
  }

  @Test
  void filterProductToCustomer() {
    customerDto.setCustomerType(ConstantsCustomerBusiness.BUSINESS.name());
    Assertions.assertNotNull(filterProductHelper.filterProductToCustomer(customerDto));
  }

  @Test
  void searchProductRegister() {
    Assertions.assertNotNull(filterProductHelper.searchProductRegister(Mono.just(customerDto), Mono.just(productDtoList)));
  }
}