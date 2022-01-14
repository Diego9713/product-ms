package bootcamp.com.productms.expose;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.dto.ProductCustomerDto;
import bootcamp.com.productms.model.dto.ProductDto;
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

  @GetMapping("")
  public Flux<ProductDto> findAllProduct() {
    return productService.findAllProduct();
  }

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

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Product>> findOneProduct(@PathVariable String id) {
    return productService.findByIdProduct(id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));

  }

  @PostMapping("")
  public Mono<ResponseEntity<ProductDto>> saveProduct(@RequestBody ProductDto product) {
    return productService.createProduct(product)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  @PostMapping("/registercustomer")
  public Mono<ResponseEntity<ProductDto>> registerProductToCustomer(@RequestParam(name = "account") String account, @RequestParam(name = "dni") String dni) {
    return productService.registerProductToCustomer(account, dni)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody ProductDto product) {
    return productService.updateProduct(product, id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<ProductDto>> removeProduct(@PathVariable("id") String id) {
    return productService.removeProduct(id)
      .flatMap(p -> Mono.just(ResponseEntity.ok().body(p)))
      .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }
}

