package bootcamp.com.productms.business.impl;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.business.helper.WebClientCustomerHelper;
import bootcamp.com.productms.model.Customer;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.repository.IProductRepository;
import bootcamp.com.productms.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service("ProductService")
@Slf4j
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository objIProductRepository;

    @Autowired
    private WebClientCustomerHelper helper;

    @Value("${variable.state}")
    private String active;

    @Override
    @Transactional(readOnly = true)
    public Flux<Product> findAllProduct() {
        return objIProductRepository.findAll().filter(e -> e.getStatus().equalsIgnoreCase(active));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Product> findByIdProduct(String id) {
        return objIProductRepository.findById(id).switchIfEmpty(Mono.empty()).filter(e -> e.getStatus().equalsIgnoreCase(active));
    }

    @Override
    @Transactional
    public Mono<Customer> createProduct(Product product) {
        Mono<Customer> customer = helper.findCustomer(product.getCustomer());
        Product filterProduct = AppUtil.filterProduct(product);
        Mono<List<Product>> productList = customer.flatMap(e -> objIProductRepository.findByCustomer(e.getId()).collectList());
        Mono<Boolean> isSave = customer.flatMap(c -> productList.flatMap(l -> Mono.just(AppUtil.isSave(c, filterProduct, l))));
        return isSave.flatMap(save -> save ? objIProductRepository.save(filterProduct).flatMap(helper::saveCustomerWithProduct) : Mono.empty());

    }

    @Override
    @Transactional
    public Mono<Product> updateProduct(Product product, String id) {
        return objIProductRepository.findById(id).switchIfEmpty(Mono.empty())
                .doOnNext(p -> {
                    BeanUtils.copyProperties(product, p);
                    p.setId(id);
                    p.setUpdateAt(new Date());
                }).flatMap(objIProductRepository::save);
    }

    @Override
    @Transactional
    public Mono<Product> removeProduct(String id) {
        return objIProductRepository.findById(id).switchIfEmpty(Mono.empty())
                .doOnNext(p -> p.setStatus("inactive"))
                .flatMap(objIProductRepository::save);
    }
}


