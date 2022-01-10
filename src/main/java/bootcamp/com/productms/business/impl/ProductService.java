package bootcamp.com.productms.business.impl;

import bootcamp.com.productms.business.IProductService;
import bootcamp.com.productms.business.helper.FilterProductHelper;
import bootcamp.com.productms.business.helper.WebClientCustomerHelper;
import bootcamp.com.productms.model.CustomerDto;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.model.ProductCustomerDto;
import bootcamp.com.productms.model.ProductDto;
import bootcamp.com.productms.repository.IProductRepository;
import bootcamp.com.productms.utils.AppUtil;
import bootcamp.com.productms.utils.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service("ProductService")
@Slf4j
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private WebClientCustomerHelper webClientCustomerHelper;

    @Autowired
    private FilterProductHelper filterProductHelper;

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductDto> findAllProduct() {
        log.info("findAll product >>>");
        return productRepository.findAll().map(AppUtil::entityToProductDto).filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Product> findByIdProduct(String id) {
        log.info("findOne product >>>");
        return productRepository.findById(id).switchIfEmpty(Mono.empty()).filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductDto> findByProductNumber(String id) {
        log.info("findBy productNumber >>>");
        return productRepository.findByAccountNumber(id).switchIfEmpty(Mono.empty())
                .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name())).map(AppUtil::entityToProductDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductCustomerDto> findProductByCustomer(String customerId) {
        log.info("find productByCustomer >>>");
        return productRepository.findByCustomer(customerId).map(AppUtil::entityToProductCustomerDto).switchIfEmpty(Mono.empty())
                .filter(product -> product.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name()));
    }

    @Override
    @Transactional
    public Mono<ProductDto> createProduct(Product product) {
        log.info("save product >>>");
        Mono<CustomerDto> customer = webClientCustomerHelper.findCustomer(product.getCustomer());
        Product filterProduct = filterProductHelper.createObjectProduct(product);

        Mono<List<Product>> productList = customer.flatMap(findCustomer -> productRepository.findByCustomer(findCustomer.getId())
                .filter(findProduct -> findProduct.getStatus().equalsIgnoreCase(CommonConstants.ACTIVE.name())).collectList());

        Mono<Boolean> isSave = customer.flatMap(findCustomer -> productList.flatMap(findProductList -> Mono.just(filterProductHelper.isSave(findCustomer, filterProduct, findProductList))));
        return isSave.flatMap(save -> save ? productRepository.save(filterProduct) : Mono.empty()).map(AppUtil::entityToProductDto);

    }

    @Override
    @Transactional
    public Mono<ProductDto> registerProductToCustomer(String accountId, String dni) {
        log.info("register productByCustomer >>>");
        Mono<List<Product>> collectList = productRepository.findByAccountNumber(accountId).collectList();
        Mono<Product> productMono = productRepository.findByAccountNumber(accountId).next();
        Mono<CustomerDto> customer = webClientCustomerHelper.findCustomerByDni(dni);
        Mono<Boolean> isFind = filterProductHelper.searchProductRegister(customer,collectList);

        return webClientCustomerHelper.findCustomerByDni(dni)
                .switchIfEmpty(Mono.empty())
                .flatMap(customerDto -> isFind.flatMap(find -> !find ? productMono.doOnNext(product -> {
                    product.setCustomer(customerDto.getId());
                    product.setId(null);
                }).flatMap(product -> filterProductHelper.filterProductToCustomer(customerDto)
                        .flatMap(isPermission -> isPermission ? productRepository.save(product): Mono.empty())): Mono.empty())).map(AppUtil::entityToProductDto);

    }

    @Override
    @Transactional
    public Mono<Product> updateProduct(Product product, String id) {
        log.info("update product >>>");
        return productRepository.findById(id).switchIfEmpty(Mono.empty())
                .flatMap(findByProduct -> filterProductHelper.updateObjectProduct(product, findByProduct))
                .flatMap(productRepository::save);
    }

    @Override
    @Transactional
    public Mono<ProductDto> removeProduct(String id) {
        log.info("remove product >>>");
        return productRepository.findById(id).switchIfEmpty(Mono.empty())
                .doOnNext(p -> p.setStatus(CommonConstants.INACTIVE.name()))
                .flatMap(productRepository::save).map(AppUtil::entityToProductDto);
    }
}


