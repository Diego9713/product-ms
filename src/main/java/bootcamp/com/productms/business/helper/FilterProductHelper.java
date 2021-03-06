package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.CustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.utils.CommonConstants;
import bootcamp.com.productms.utils.ConstantsCustomerBusiness;
import bootcamp.com.productms.utils.ConstantsCustomerPersonal;
import bootcamp.com.productms.utils.ConstantsPersonal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FilterProductHelper {

  /**
   * Method to create a product object.
   *
   * @param product -> is the product entered by the user.
   * @return the product complete.
   */
  public ProductDto createObjectProduct(ProductDto product, CustomerDto customerDto, List<Product> productDtoList) {
    product.setCreatedAt(LocalDate.now());
    product.setUpdateAt(LocalDate.now());
    product.setStatus(CommonConstants.ACTIVE.name());
    product.setAccountType(product.getAccountType().toUpperCase());
    ProductDto productDto = addAccountAndLevel(product, productDtoList);
    return filterProduct(productDto, customerDto);
  }

  /**
   * Method to assign the account number, sub_account and level.
   *
   * @param product        -> is the product entered by the user.
   * @param productDtoList -> number of products created by the customer.
   * @return the product complete.
   */
  public ProductDto addAccountAndLevel(ProductDto product, List<Product> productDtoList) {

    ProductDto productDto = new ProductDto();

    if (productDtoList.isEmpty()) {
      product.setAccountNumber(UUID.randomUUID().toString());
      product.setSubAccountNumber(product.getAccountNumber());
      product.setLevel(1);
    } else {
      product.setAccountNumber(productDtoList.get(0).getAccountNumber());
      product.setSubAccountNumber(productDtoList.get(0).getAccountNumber() + " - " + (productDtoList.size() + 1));
      product.setLevel(productDtoList.size() + 1);
    }
    BeanUtils.copyProperties(product, productDto);
    return productDto;
  }


  /**
   * Method to update a product object.
   *
   * @param product     -> is the product entered by the user.
   * @param findProduct -> is the product found in the database.
   * @return the product complete.
   */
  public Mono<ProductDto> updateObjectProduct(ProductDto product, Product findProduct, CustomerDto customerDto) {
    product.setId(findProduct.getId());
    product.setAccountNumber(findProduct.getAccountNumber());
    product.setSubAccountNumber(findProduct.getSubAccountNumber());
    product.setLevel(findProduct.getLevel());
    product.setCreatedAt(findProduct.getCreatedAt());
    product.setUpdateAt(LocalDate.now());
    product.setStatus(product.getStatus().toUpperCase());
    product.setAccountType(findProduct.getAccountType().toUpperCase());
    product.setMaxTransactNumber(findProduct.getMaxTransactNumber());
    return Mono.just(filterProduct(product, customerDto));
  }

  /**
   * Method to filter and assign default values to the product regarding the type of account.
   *
   * @param product     -> is the product entered by the user.
   * @param customerDto -> is the wanted customer.
   * @return the product complete.
   */
  public ProductDto filterProduct(ProductDto product, CustomerDto customerDto) {
    switch (product.getAccountType()) {
      case "SAVING":
        product.setMaxTransactNumber(10);
        product.setMaintenanceCommission(0.0);
        product.setCreditLimit(0.0);
        product.setTransactNumberDay(null);
        product.setMaintenanceCommissionDay(null);
        product.setAverageDailyBalanceDay(LocalDate.now());
        product.setExpiredDate(null);
        if (customerDto.getCustomerType().equalsIgnoreCase(ConstantsCustomerPersonal.PERSONAL_VIP.name())) {
          product.setAverageDailyBalance(product.getAmount());
          product.setMinimumAverageAmount(product.getAmount());
        } else {
          product.setAverageDailyBalance(product.getAmount());
          product.setMinimumAverageAmount(0);
        }
        break;
      case "CURRENT":
        product.setMaxTransactNumber(10);
        product.setCreditLimit(0.0);
        product.setTransactNumberDay(null);
        product.setMinimumAverageAmount(0);
        product.setAverageDailyBalance(product.getAmount());
        product.setAverageDailyBalanceDay(LocalDate.now());
        product.setExpiredDate(null);
        if (product.getMaintenanceCommission() < 0 || product.getMaintenanceCommission() == 0) {
          if (customerDto.getCustomerType().equalsIgnoreCase(ConstantsCustomerBusiness.BUSINESS_PYME.name())) {
            product.setMaintenanceCommission(0);
          } else {
            product.setMaintenanceCommission(5.00);
          }
        }
        LocalDateTime commission = LocalDateTime.now();
        product.setMaintenanceCommissionDay(commission.plusDays((long) 30 - commission.getDayOfMonth()));
        break;
      case "FIXED_TERM":
        product.setMaxTransactNumber(1);
        product.setMaintenanceCommission(0.0);
        product.setCreditLimit(0.0);
        product.setMinimumAverageAmount(0);
        product.setAverageDailyBalance(product.getAmount());
        product.setAverageDailyBalanceDay(LocalDate.now());
        product.setMaintenanceCommissionDay(null);
        product.setExpiredDate(null);
        LocalDate transactDay = LocalDate.now();
        product.setTransactNumberDay(transactDay.plusDays(10));
        break;
      default:
        product.setMaintenanceCommission(0.0);
        product.setMaxTransactNumber(-1);
        product.setMinimumAverageAmount(0);
        product.setAverageDailyBalance(product.getAmount());
        product.setTransactNumberDay(LocalDate.now());
        product.setAverageDailyBalanceDay(LocalDate.now());
        product.setMaintenanceCommissionDay(null);
        if (product.getCreditLimit() <= 0) {
          product.setCreditLimit(1000);
        }
        if (product.getExpiredDate() == null) {
          product.setExpiredDate(LocalDate.now().plusYears(1));
        }
        break;
    }
    return product;
  }

  /**
   * Method to condition the storage of the product.
   *
   * @param customerDto -> is the wanted customer.
   * @param product     -> is the product filter.
   * @param productList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public boolean isSave(CustomerDto customerDto, ProductDto product, List<Product> productList) {
    boolean isSave = false;
    if (Arrays.stream(ConstantsCustomerPersonal.values())
        .anyMatch(constants -> customerDto.getCustomerType().equalsIgnoreCase(constants.name()))) {
      if (customerDto.getCustomerType().equalsIgnoreCase(ConstantsCustomerPersonal.PERSONAL.name())
          && product.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name())
          && isPermissionPersonal(productList, product)) {
        isSave = true;
      }
      if (isAccountTypePersonal(product, customerDto, productList) && isPermissionPersonal(productList, product)) {
        isSave = true;
      }

    } else {
      if (isAccountTypeBusiness(product, customerDto, productList) && isPermissionBusiness(productList)) {
        isSave = true;
      }

    }
    return isSave;
  }

  /**
   * Method to know the type of account.
   *
   * @param product     -> is the product entered by the user.
   * @param customerDto -> is the wanted customer.
   * @param productList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public boolean isAccountTypePersonal(ProductDto product, CustomerDto customerDto, List<Product> productList) {
    boolean isSave = false;
    if (product.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name())
        && customerDto.getCustomerType().equalsIgnoreCase(ConstantsCustomerPersonal.PERSONAL_VIP.name())) {
      isSave = filterAccountType(productList);
    } else {
      if (product.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name())
          || product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name())
          || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
        isSave = true;
      }
    }
    return isSave;
  }

  /**
   * Method to know the type of customer.
   *
   * @param product     -> is the product entered by the user.
   * @param customerDto -> is the wanted customer.
   * @param productList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public boolean isAccountTypeBusiness(ProductDto product, CustomerDto customerDto, List<Product> productList) {
    boolean isSave = false;
    if (customerDto.getCustomerType().equalsIgnoreCase(ConstantsCustomerBusiness.BUSINESS_PYME.name())
        && product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name())) {
      isSave = filterAccountType(productList);
    } else {
      if (product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name())
          || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
        isSave = true;
      }
    }
    return isSave;
  }

  /**
   * Method to save for personal vip.
   *
   * @param productList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public boolean filterAccountType(List<Product> productList) {
    boolean isSave = false;
    for (Product product : productList) {
      if (product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())
          && product.getExpiredDate().isAfter(LocalDate.now())) {
        isSave = true;
        break;
      }
    }
    return isSave;
  }

  /**
   * Method to condition the number of accounts personal.
   *
   * @param productList  -> is the list of accounts assigned to is customer.
   * @param otherProduct -> is the product entered by the user.
   * @return a condition for the storage of the product.
   */
  public boolean isPermissionPersonal(List<Product> productList, ProductDto otherProduct) {
    boolean isPermission = false;
    if (productList.isEmpty()) {
      isPermission = true;
    } else {
      for (Product product : productList) {
        if (product.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name())
            && Arrays.stream(ConstantsPersonal.values())
            .anyMatch(p -> p.toString().equalsIgnoreCase(otherProduct.getAccountType()))) {
          isPermission = filterPermissionPersonalVip(productList, otherProduct);
        } else {
          if (product.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name())
              || product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name())
              || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
            isPermission = filterPermissionPersonal(productList, otherProduct);
          }
        }
      }
    }
    return isPermission;
  }

  /**
   * Method to condition the number of accounts business.
   *
   * @param productList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public boolean isPermissionBusiness(List<Product> productList) {
    boolean isPermission = false;
    if (productList.isEmpty()) {
      isPermission = true;
    } else {
      for (Product product : productList) {

        if (product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())
            && product.getExpiredDate().isBefore(LocalDate.now())) {
          isPermission = false;
          break;
        } else {
          isPermission = true;
        }
      }
    }
    return isPermission;
  }

  /**
   * Method to know the type of account.
   *
   * @param productList  -> is the list of accounts assigned to is customer.
   * @param otherProduct -> is the product entered by the user.
   * @return a condition for the storage of the product.
   */
  public boolean filterPermissionPersonalVip(List<Product> productList, ProductDto otherProduct) {
    boolean isPermission;
    //change
    if (productList.stream()
        .anyMatch(p -> p.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())
          && p.getExpiredDate().isAfter(LocalDate.now())
          && productList.size() != 1)) {
      isPermission = otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name());
    } else {
      isPermission = true;
    }

    return isPermission;
  }

  /**
   * Method to know the type of account.
   *
   * @param productList  -> is the list of accounts assigned to is customer.
   * @param otherProduct -> is the product entered by the user.
   * @return a condition for the storage of the product.
   */
  public boolean filterPermissionPersonal(List<Product> productList, ProductDto otherProduct) {
    boolean isPermission = false;
    if (productList.size() == 1
        && !otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())
        && Arrays.stream(ConstantsPersonal.values())
        .anyMatch(p -> p.toString().equalsIgnoreCase(otherProduct.getAccountType()))) {
      //change
      if (productList.stream().anyMatch(product -> product.getAccountType()
          .equalsIgnoreCase(CommonConstants.CREDIT.name()) && product.getExpiredDate().isAfter(LocalDate.now()))) {
        isPermission = productList.stream().noneMatch(p ->
          p.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name())
            || p.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()));
      }

    } else {
      isPermission = filterPermissionPersonalCredit(productList, otherProduct);
    }

    return isPermission;
  }

  /**
   * Method to know the type of account credit.
   *
   * @param productList  -> is the list of accounts assigned to is customer.
   * @param otherProduct -> is the product entered by the user.
   * @return a condition for the storage of the product.
   */
  public boolean filterPermissionPersonalCredit(List<Product> productList, ProductDto otherProduct) {
    boolean isPermission = false;

    Optional<Product> productDtoMono = productList.stream().findFirst();
    if (productList.size() == 1 && otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())
        && productDtoMono.stream().noneMatch(product -> product.getAccountType()
        .equalsIgnoreCase(CommonConstants.CREDIT.name()))) {
      isPermission = true;
    } else {
      if (productList.size() == 1
          && productList.stream().anyMatch(product -> product.getAccountType()
          .equalsIgnoreCase(CommonConstants.CREDIT.name()))
          && productList.stream().anyMatch(product -> product.getExpiredDate().isAfter(LocalDate.now()))
          && otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name())
          || otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name())) {
        isPermission = true;
      }
    }
    return isPermission;
  }

  /**
   * Method to filter the business-type customer record.
   *
   * @param customerDto -> is the wanted customer.
   * @return a condition for the storage of the product.
   */
  public Mono<Boolean> filterProductToCustomer(CustomerDto customerDto) {
    boolean isPermission = Arrays.stream(ConstantsCustomerBusiness.values())
        .anyMatch(constants -> customerDto.getCustomerType().equalsIgnoreCase(constants.name()));
    return Mono.just(isPermission);
  }

  /**
   * Method to filter the record.
   *
   * @param customerDto -> is the wanted customer.
   * @param collectList -> is the list of accounts assigned to is customer.
   * @return a condition for the storage of the product.
   */
  public Mono<Boolean> searchProductRegister(Mono<CustomerDto> customerDto, Mono<List<Product>> collectList) {

    return collectList.flatMap(productList -> customerDto.map(findCustomer -> productList.stream()
      .anyMatch(product -> product.getCustomer().equals(findCustomer.getId()))));

  }

  /**
   * Method to filter to generate SPD.
   *
   * @param product -> is the wanted customer.
   * @return a condition for the storage of the product.
   */
  public Mono<Product> filterGenerateSpd(Product product) {

    if (LocalDateTime.now().getHour() > 15 && LocalDate.now().getMonth()
        .equals(product.getAverageDailyBalanceDay().getMonth())) {
      product.setAverageDailyBalance(product.getAmount() + product.getAverageDailyBalance());
      product.setMinimumAverageAmount(product.getAverageDailyBalance() / LocalDate.now().getDayOfMonth());
    } else {
      if (!LocalDate.now().getMonth().equals(product.getAverageDailyBalanceDay().getMonth())) {
        product.setAverageDailyBalanceDay(LocalDate.now());
        product.setAverageDailyBalance(product.getAmount());
      }
    }
    return Mono.just(product);
  }

  /**
   * Method to add attributes to product.
   *
   * @param customerDto -> is the wanted customer.
   * @param product     -> find product.
   * @return a condition for the storage of the product.
   */
  public Mono<Product> setProductAttributes(CustomerDto customerDto, Product product) {
    product.setCustomer(customerDto.getId());
    product.setId(null);
    return Mono.just(product);
  }

}
