package bootcamp.com.productms.utils;

import bootcamp.com.productms.model.Customer;
import bootcamp.com.productms.model.Product;
import bootcamp.com.productms.repository.IProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AppUtil {

    public static Product filterProduct(Product product) {
        log.info(""+product);
        product.setAccountNumber(UUID.randomUUID().toString());
        product.setCreatedAt(new Date());
        product.setUpdateAt(null);
        product.setStatus("active");
        switch (product.getAccountType()) {
            case "saving":
                product.setMaintenanceCommission(0.0);
                product.setCreditLimit(0.0);
                product.setTransactNumberDay(null);
                if (product.getMaxTransactNumber() < 0 || product.getMaxTransactNumber() == 0)
                    product.setMaxTransactNumber(1);
                break;
            case "current":
                product.setCreditLimit(0.0);
                product.setTransactNumberDay(null);
                if (product.getMaintenanceCommission() < 0 || product.getMaintenanceCommission() == 0)
                    product.setMaintenanceCommission(10.0);
                product.setMaxTransactNumber(-1);
                break;
            case "fixedTerm":
                product.setMaintenanceCommission(0.0);
                product.setMaxTransactNumber(1);
                product.setCreditLimit(0.0);
                if (product.getTransactNumberDay() == null)
                    product.setTransactNumberDay(new Date());
                break;
            default:
                product.setMaintenanceCommission(0.0);
                product.setMaxTransactNumber(-1);
                product.setTransactNumberDay(null);
                if (product.getCreditLimit() <= 0)
                    product.setCreditLimit(100);
                if (product.getAccountType().equalsIgnoreCase("free"))
                    product.setAccountType("credit");
                    product.setAccountNumber(null);
                break;
        }
        return product;
    }
    public static boolean isSave(Customer customer, Product product,List<Product> productList){
        boolean isSave = false;
        if (customer.getCustomerType().equalsIgnoreCase("personal")){
             if (product.getAccountType().equalsIgnoreCase("FixTerm") && isPermisionPersonal(productList) ){
                 isSave = true;
             }
            if (product.getAccountType().equalsIgnoreCase("saving") || product.getAccountType().equalsIgnoreCase("current")|| product.getAccountType().equalsIgnoreCase("credit")){
                if (isPermisionPersonal(productList)){
                    isSave = true;
                }
            }

        }else{
            if (product.getAccountType().equalsIgnoreCase("current") ||product.getAccountType().equalsIgnoreCase("credit") && isPermisionBusiness(productList) ){
                isSave = true;
            }else{
                isSave = false;
            }

        }
        return isSave;
    }
    public static boolean isPermisionPersonal(List<Product> productList){
        boolean isPermission = false;
        if (productList.isEmpty()){
            isPermission = true;
        }else {
            for (Product product : productList) {
                if (product.getAccountType().equalsIgnoreCase("FixTerm")) {
                    isPermission = true;
                }
                if (product.getAccountType().equalsIgnoreCase("saving") || product.getAccountType().equalsIgnoreCase("current") || product.getAccountType().equalsIgnoreCase("credit")){
                    isPermission = false;
                }
            }
        }
        return isPermission;
    }
    public static boolean isPermisionBusiness(List<Product> productList){
        boolean isPermission = false;
        if (productList.isEmpty()){
            isPermission = true;
        }else {
            for (Product product : productList) {
                if (product.getAccountType().equalsIgnoreCase("current") || product.getAccountType().equalsIgnoreCase("credit")) {
                    isPermission = true;
                }
                if (product.getAccountType().equalsIgnoreCase("saving") || product.getAccountType().equalsIgnoreCase("FixTerm")){
                    isPermission = false;
                }
            }
        }
        return isPermission;
    }
}
