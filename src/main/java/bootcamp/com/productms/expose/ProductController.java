package bootcamp.com.productms.expose;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
import bootcamp.com.productms.model.dto.ProductSpdDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {
  @Autowired
  @Qualifier("ProductService")
  private IProductService productService;

  /**
   * Method to list all products.
   *
   * @return list of all active products.
   */
  @GetMapping("")
  public Flux<ProductDto> findAllProduct() {
    return productService.findAllProduct();
  }

  /**
   * Method to list one product to account.
   *
   * @param account -> Account identifier.
   * @return a product.
   */
  @GetMapping("/accountnumber/{account}")
  public Flux<ProductDto> findOneProductNumber(@PathVariable("account") String account) {
    return productService.findByProductNumber(account);
  }

  /**
   * Method to consult the balances available in the products.
   *
   * @param customerId -> customer identifier to perform the search.
   * @return the selected customer's products.
   */
  @GetMapping("/customer/{customerId}")
  public Flux<ProductCustomerDto> findProductByCustomer(@PathVariable("customerId") String customerId) {
    return productService.findProductByCustomer(customerId);
  }

  /**
   * Lists the daily averages by month.
   *
   * @param customerId -> client identifier.
   * @param dateTime   -> registration date.
   * @return a product.
   */
  @GetMapping("/averagedailydalance/{customerId}")
  public Flux<ProductSpdDto> findAverageDailyBalance(@PathVariable("customerId") String customerId,
                                                     @RequestParam("date") String dateTime) {
    return productService.findAverageDailyBalance(customerId, dateTime);
  }

  /**
   * Method to list a specific product.
   *
   * @param id -> is identified of the product.
   * @return a products.
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Product>> findOneProduct(@PathVariable String id) {
    return productService.findByIdProduct(id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));

  }

  /**
   * Method of saving a product.
   *
   * @param product -> object with account data.
   * @return the product saved.
   */
  @CircuitBreaker(name = "postProductCB", fallbackMethod = "fallBackPostProduct")
  @PostMapping("")
  public Mono<ResponseEntity<ProductDto>> saveProduct(@RequestBody ProductDto product) {
    return productService.createProduct(product)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to register an account to multiple clients.
   *
   * @param account -> account identifier.
   * @param dni     -> customer document.
   * @return account register with customer.
   */
  @CircuitBreaker(name = "postProductRegisterCustomerCB", fallbackMethod = "fallBackPostProductRegisterCustomer")
  @PostMapping("/registercustomer")
  public Mono<ResponseEntity<ProductDto>> registerProductToCustomer(@RequestParam(name = "account") String account,
                                                                    @RequestParam(name = "dni") String dni) {
    return productService.registerProductToCustomer(account, dni)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to generate the average daily balance per month per product.
   *
   * @param id -> product identifier.
   * @return an object with the daily average balance.
   */
  @PutMapping("/generate/{productId}")
  public Mono<ResponseEntity<ProductDto>> generateSpd(@PathVariable("productId") String id) {
    return productService.generateSpd(id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  /**
   * Method to update a product.
   *
   * @param product -> object with account data.
   * @param id      -> is identified of the product.
   * @return a product.
   */
  @CircuitBreaker(name = "putProductCB", fallbackMethod = "fallBackPutProduct")
  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody ProductDto product) {
    return productService.updateProduct(product, id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  /**
   * Method change status of product.
   *
   * @param id -> is identified of the product.
   * @return a product.
   */
  @CircuitBreaker(name = "deleteProductCB", fallbackMethod = "fallBackDeleteProduct")
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<ProductDto>> removeProduct(@PathVariable("id") String id) {
    return productService.removeProduct(id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  /**
   * Method CircuitBreaker to save Product.
   *
   * @param ex -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackPostProduct(@RequestBody ProductDto product,
                                                          RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("Saving Product with "
      + product.getAccountType()
      + " not available method"));
  }

  /**
   * Method CircuitBreaker to save Product with customer.
   *
   * @param ex -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackPostProductRegisterCustomer(@RequestParam(name = "account") String account,
                                                                          @RequestParam(name = "dni") String dni,
                                                                          RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("Register Product with dni: "
      + dni
      + " account: "
      + account
      + " not available microservice"));
  }

  /**
   * Method CircuitBreaker to update Product.
   *
   * @param ex -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackPutProduct(@PathVariable String id,
                                                         @RequestBody ProductDto product,
                                                         RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("updating Product with id: "
      + id
      + " account: "
      + product.getAccountNumber()
      + " not available"));
  }

  /**
   * Method CircuitBreaker to delete Product.
   *
   * @param ex -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackDeleteProduct(@PathVariable String id,
                                                            RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("delete Product with id: "
      + id
      + " not available"));
  }
}

