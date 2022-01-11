package bootcamp.com.productms.business.helper;

import bootcamp.com.productms.model.CustomerDto;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.utils.CommonConstants;
import bootcamp.com.productms.utils.ConstantsPersonal;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class FilterProductHelper {
    /**
     * Method to create a product object.
     *
     * @param product -> is the product entered by the user.
     * @return the product complete.
     */
    public Product createObjectProduct(Product product) {
        product.setAccountNumber(UUID.randomUUID().toString());
        product.setCreatedAt(new Date());
        product.setUpdateAt(new Date());
        product.setStatus(CommonConstants.ACTIVE.name());
        product.setAccountType(product.getAccountType().toUpperCase());
        return filterProduct(product);
    }

    /**
     * Method to update a product object.
     *
     * @param product     -> is the product entered by the user.
     * @param findProduct -> is the product found in the database.
     * @return the product complete.
     */
    public Mono<Product> updateObjectProduct(Product product, Product findProduct) {
        product.setAccountNumber(findProduct.getAccountNumber());
        product.setCreatedAt(findProduct.getCreatedAt());
        product.setUpdateAt(new Date());
        product.setStatus(product.getStatus().toUpperCase());
        product.setAccountType(findProduct.getAccountType().toUpperCase());
        return Mono.just(filterProduct(product));
    }

    /**
     * Method to filter and assign default values to the product regarding the type of account.
     *
     * @param product-> is the product entered by the user.
     * @return the product complete.
     */
    public Product filterProduct(Product product) {
        switch (product.getAccountType()) {
            case "SAVING":
                product.setMaintenanceCommission(0.0);
                product.setCreditLimit(0.0);
                product.setTransactNumberDay(null);
                product.setMaintenanceCommissionDay(null);
                if (product.getMaxTransactNumber() < 0 || product.getMaxTransactNumber() == 0)
                    product.setMaxTransactNumber(1);
                break;
            case "CURRENT":
                product.setCreditLimit(0.0);
                product.setTransactNumberDay(null);
                if (product.getMaintenanceCommission() < 0 || product.getMaintenanceCommission() == 0)
                    product.setMaintenanceCommission(50.0);
                LocalDateTime commission = LocalDateTime.now();
                product.setMaintenanceCommissionDay(commission.plusDays(30 - commission.getDayOfMonth()));
                product.setMaxTransactNumber(-1);
                break;
            case "FIXED_TERM":
                product.setMaintenanceCommission(0.0);
                product.setMaxTransactNumber(-1);
                product.setCreditLimit(0.0);
                product.setMaintenanceCommissionDay(null);
                if (product.getTransactNumberDay() == null) {
                    LocalDateTime transactDay = LocalDateTime.now();
                    product.setTransactNumberDay(transactDay.plusDays(10));
                }
                break;
            default:
                /* TODO to be defined */
                product.setMaintenanceCommission(0.0);
                product.setMaxTransactNumber(-1);
                product.setTransactNumberDay(LocalDateTime.now());
                product.setMaintenanceCommissionDay(null);
                if (product.getCreditLimit() <= 0)
                    product.setCreditLimit(1000);
                break;
        }
        return product;
    }

    /**
     * Method to condition the storage of the product.
     *
     * @param customerDto -> is the wanted customer.
     * @param product     -> is the product entered by the user.
     * @param productList -> is the list of accounts assigned to is customer.
     * @return a condition for the storage of the product.
     */
    public boolean isSave(CustomerDto customerDto, Product product, List<Product> productList) {
        boolean isSave = false;
        if (customerDto.getCustomerType().equalsIgnoreCase(CommonConstants.PERSONAL.name())) {
            if (product.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name()) && isPermisionPersonal(productList, product)) {
                isSave = true;
            }
            if (product.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
                if (isPermisionPersonal(productList, product)) {
                    isSave = true;
                }
            }

        } else {
            if (product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name()) && isPermisionBusiness(productList)) {
                isSave = true;
            } else {
                isSave = false;
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
    public boolean isPermisionPersonal(List<Product> productList, Product otherProduct) {
        boolean isPermission = false;
        if (productList.isEmpty()) {
            isPermission = true;
        } else {
            for (Product product : productList) {
                if (product.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name()) && Arrays.stream(ConstantsPersonal.values()).anyMatch(p -> p.toString().equalsIgnoreCase(otherProduct.getAccountType()))) {
                    if (productList.stream().anyMatch(p -> p.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name()) && productList.size() != 1)) {
                        if (otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.FIXED_TERM.name())) {
                            isPermission = true;
                        } else {
                            isPermission = false;
                        }
                    } else {
                        isPermission = true;
                    }
                } else {
                    if (product.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
                        if (productList.size() == 1 && !otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name()) && Arrays.stream(ConstantsPersonal.values()).anyMatch(p -> p.toString().equalsIgnoreCase(otherProduct.getAccountType()))) {
                            if (productList.stream().anyMatch(p -> p.getAccountType().equalsIgnoreCase(CommonConstants.SAVING.name()) || p.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()))) {
                                isPermission = false;
                            } else {
                                isPermission = true;
                            }
                        } else {
                            if (productList.size() == 1 && otherProduct.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
                                isPermission = true;
                            } else {
                                isPermission = false;
                            }
                        }
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
    public boolean isPermisionBusiness(List<Product> productList) {
        boolean isPermission = false;
        if (productList.isEmpty()) {
            isPermission = true;
        } else {
            for (Product product : productList) {
                if (product.getAccountType().equalsIgnoreCase(CommonConstants.CURRENT.name()) || product.getAccountType().equalsIgnoreCase(CommonConstants.CREDIT.name())) {
                    isPermission = true;
                }
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
        boolean isPermission = false;
        if (customerDto.getCustomerType().equalsIgnoreCase(CommonConstants.BUSINESS.name())) {
            isPermission = true;
        }
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
}
