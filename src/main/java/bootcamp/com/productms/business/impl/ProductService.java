package bootcamp.com.productms.business.impl;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.business.helper.FilterProductHelper;
import bootcamp.com.productms.business.helper.WebClientCardHelper;
import bootcamp.com.productms.business.helper.WebClientCustomerHelper;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.CustomerDto;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import bootcamp.com.productms.repository.IProductRepository;
import bootcamp.com.productms.utils.AppUtil;
import bootcamp.com.productms.utils.CommonConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("ProductService")
@Slf4j
public class ProductService implements IProductService {

  @Autowired
  private IProductRepository productRepository;

  @Autowired
  private WebClientCustomerHelper webClientCustomerHelper;

  @Autowired
  private WebClientCardHelper webClientCardHelper;

  @Autowired
  private FilterProductHelper filterProductHelper;

  /**
   * Method of listing all available products.
   *
   * @return a list of products.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<ProductDto> findAllProduct() {
    log.info("findAll product >>>");
    return productRepository.findAll()
      .map(AppUtil::entityToProductDto)
      .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
  }

  /**
   * Method to list a specific product.
   *
   * @param id -> is identified of the product.
   * @return a products.
   */
  @Override
  @Transactional(readOnly = true)
  public Mono<Product> findByIdProduct(String id) {
    log.info("findOne product >>>");
    return productRepository.findById(id)
      .switchIfEmpty(Mono.empty())
      .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
  }

  /**
   * Lists the daily averages by month.
   *
   * @param id       -> client identifier.
   * @param dateTime -> registration date.
   * @return a product.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<ProductSpdDto> findAverageDailyBalance(String id, String dateTime) {
    LocalDate localDate = LocalDate.parse(dateTime);
    return productRepository.findByCustomerAndAverageDailyBalanceDay(id, localDate)
      .map(AppUtil::entityToProductSpdDto);
  }

  /**
   * Method search product to account type and date.
   *
   * @param accountType -> value of type account product.
   * @param from        -> start date.
   * @param until       -> ending date.
   * @return a list products.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<ProductDto> findByAccountTypeAndCreatedAtBetween(String accountType, String from, String until) {
    LocalDate fromDate = LocalDate.parse(from);
    LocalDate untilDate = LocalDate.parse(until);
    String accountUpperCase = accountType.toUpperCase();
    return productRepository.findByAccountTypeAndCreatedAtBetween(accountUpperCase, fromDate, untilDate)
      .map(AppUtil::entityToProductDto);
  }

  /**
   * Method to search for a product by account number.
   *
   * @param id -> is account number of product.
   * @return a product.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<ProductDto> findByProductNumber(String id) {
    log.info("findBy productNumber >>>");
    return productRepository.findByAccountNumber(id)
      .switchIfEmpty(Mono.empty())
      .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()))
      .map(AppUtil::entityToProductDto);
  }

  /**
   * Method to consult the balances available in the products.
   *
   * @param customerId -> customer identifier to perform the search.
   * @return the selected customer's products.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<ProductCustomerDto> findProductByCustomer(String customerId) {
    log.info("find productByCustomer >>>");
    return productRepository.findByCustomer(customerId)
      .map(AppUtil::entityToProductCustomerDto)
      .switchIfEmpty(Mono.empty())
      .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
  }

  /**
   * Method to generate the average daily balance per month per product.
   *
   * @param id -> product identifier.
   * @return an object with the daily average balance.
   */
  @Override
  @Transactional
  public Mono<ProductDto> generateSpd(String id) {
    return productRepository.findById(id)
      .flatMap(product -> filterProductHelper.filterGenerateSpd(product))
      .flatMap(productRepository::save)
      .map(AppUtil::entityToProductDto);
  }

  /**
   * Method of saving a product.
   *
   * @param product -> object with account data.
   * @return the product saved.
   */
  @Override
  @Transactional
  public Mono<ProductDto> createProduct(ProductDto product) {
    log.info("save product >>>");

    Mono<CustomerDto> customer = webClientCustomerHelper.findCustomer(product.getCustomer());

    Flux<Product> productFlux = productRepository.findByCustomer(product.getCustomer())
        .filter(findProduct -> findProduct.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));

    Mono<List<Product>> productList = productFlux.collectList();

    Mono<ProductDto> filterProduct = customer.flatMap(customerDto -> productList
        .map(pl -> filterProductHelper.createObjectProduct(product, customerDto, pl)));

    Mono<Boolean> isSave = customer
        .flatMap(findCustomer -> productList
        .flatMap(findProductList -> filterProduct
          .flatMap(productDto -> Mono.just(filterProductHelper
            .isSave(findCustomer, productDto, findProductList)))));

    Mono<Product> newProductFilter = filterProduct.map(AppUtil::productDtoToEntity);
    return isSave.filter(Boolean.TRUE::equals)
      .flatMap(b -> newProductFilter.flatMap(productRepository::save))
        .map(AppUtil::entityToProductDto);
  }

  /**
   * Method to register an account to multiple clients.
   *
   * @param subAccount -> account identifier.
   * @param dni        -> customer document.
   * @return account register with customer.
   */
  @Override
  @Transactional
  public Mono<ProductDto> registerProductToCustomer(String subAccount, String dni) {
    log.info("register productByCustomer >>>");

    Flux<Product> productFlux = productRepository.findBySubAccountNumber(subAccount);
    Mono<List<Product>> collectList = productFlux.collectList();
    Mono<Product> productMono = productFlux.next();

    Mono<CustomerDto> customer = webClientCustomerHelper.findCustomerByDni(dni);
    Mono<Boolean> isFind = filterProductHelper.searchProductRegister(customer, collectList);

    return webClientCustomerHelper.findCustomerByDni(dni)
      .switchIfEmpty(Mono.empty())
      .flatMap(customerDto -> isFind.filter(Boolean.FALSE::equals)
          .flatMap(b -> productMono.flatMap(product -> filterProductHelper.setProductAttributes(customerDto, product)))
        .flatMap(product -> filterProductHelper.filterProductToCustomer(customerDto)
          .filter(Boolean.TRUE::equals)
          .flatMap(isPermission -> productRepository.save(product))))
      .map(AppUtil::entityToProductDto);

  }

  /**
   * Method to update a product.
   *
   * @param product -> object with account data.
   * @param id      -> is identified of the product.
   * @return a product.
   */
  @Override
  @Transactional
  public Mono<ProductDto> updateProduct(ProductDto product, String id) {
    log.info("update product >>>");
    Mono<CustomerDto> customer = webClientCustomerHelper.findCustomer(product.getCustomer());
    return productRepository.findById(id)
      .switchIfEmpty(Mono.empty())
      .flatMap(findByProduct -> customer.flatMap(customerDto ->
          filterProductHelper.updateObjectProduct(product, findByProduct, customerDto))
        .map(AppUtil::productDtoToEntity))
      .flatMap(productRepository::save).map(AppUtil::entityToProductDto);
  }

  /**
   * Method change status of product.
   *
   * @param id -> is identified of the product.
   * @return a product.
   */
  @Override
  @Transactional
  public Mono<ProductDto> removeProduct(String id) {
    log.info("remove product >>>");
    return productRepository.findById(id)
      .switchIfEmpty(Mono.empty())
      .doOnNext(p -> p.setStatus(CommonConstants.INACTIVE.name()))
      .flatMap(product -> webClientCardHelper.deleteCard(product.getId())
        .filter(Boolean.TRUE::equals)
        .flatMap(isBool -> productRepository.save(product))
        .map(AppUtil::entityToProductDto));

  }
}


