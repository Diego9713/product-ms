package bootcamp.com.productms.expose;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.model.Customer;
import bootcamp.com.productms.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    @Autowired
    @Qualifier("ProductService")
    private IProductService productService;

    @GetMapping("")
    public Flux<Product> findAllProduct(){
        return productService.findAllProduct();
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findOneProduct(@PathVariable String id){
        return productService.findByIdProduct(id)
                .flatMap(p->Mono.just(ResponseEntity.ok().body(p)))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));

    }
    @PostMapping("")
    public Mono<ResponseEntity<Customer>> saveProduct(@RequestBody Product product){
        return productService.createProduct(product)
                .flatMap(p->Mono.just(ResponseEntity.ok().body(p)))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,@RequestBody Product product){
        return productService.updateProduct(product,id)
                .flatMap(p->Mono.just(ResponseEntity.ok().body(p)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Product>> removeProduct(@PathVariable("id") String id){
        return productService.removeProduct(id)
                .flatMap(p->Mono.just(ResponseEntity.ok().body(p)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}

