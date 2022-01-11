package bootcamp.com.productms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String accountType;
    private String accountNumber;
    private String currency;
    private double amount;
    private double maintenanceCommission;
    private LocalDateTime maintenanceCommissionDay;
    private int maxTransactNumber;
    private LocalDateTime transactNumberDay;
    private double creditLimit;
    private String customer;
    private String status;
}
