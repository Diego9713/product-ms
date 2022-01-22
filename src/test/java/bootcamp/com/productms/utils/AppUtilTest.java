package bootcamp.com.productms.utils;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class AppUtilTest {

  private static final ProductDto productMock = new ProductDto();
  private static final Product product = new Product();
  private static final String id = "61db64d731dec743727907f3";
  private static final String accountType = "SAVING";
  private static final String accountNumber = "d85c241a-2eb7-40da-938c-097f30d3756f";
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
  private static final String updateBy = "pedro";
  private static final double minimumAverageAmount = 0;
  private static final double averageDailyBalance = 0;
  private static final LocalDate averageDailyBalanceDay = LocalDate.now();

  @BeforeAll
  static void setUp() {
    product.setId(id);
    product.setAccountType(accountType);
    product.setAccountNumber(accountNumber);
    product.setCurrency(currency);
    product.setAmount(amount);
    product.setMaintenanceCommission(maintenanceCommission);
    product.setMaintenanceCommissionDay(maintenanceCommissionDay);
    product.setMaxTransactNumber(maxTransactNumber);
    product.setTransactNumberDay(transactNumberDay);
    product.setCreditLimit(creditLimit);
    product.setCustomer(customer);
    product.setStatus(status);
    product.setCreatedAt(createdAt);
    product.setCreatedBy(createdBy);
    product.setUpdateAt(updateAt);
    product.setUpdateBy(updateBy);
    product.setMinimumAverageAmount(minimumAverageAmount);
    product.setAverageDailyBalance(averageDailyBalance);
    product.setAverageDailyBalanceDay(averageDailyBalanceDay);
    BeanUtils.copyProperties(product,productMock);
  }

  @Test
  void entityToProductDto() {
    Assertions.assertNotNull(AppUtil.entityToProductDto(product));
  }

  @Test
  void productDtoToEntity() {
    Assertions.assertNotNull(AppUtil.productDtoToEntity(productMock));
  }

  @Test
  void entityToProductCustomerDto() {
    Assertions.assertNotNull(AppUtil.entityToProductCustomerDto(product));
  }

  @Test
  void entityToProductSpdDto() {
    Assertions.assertNotNull(AppUtil.entityToProductSpdDto(product));
  }
}