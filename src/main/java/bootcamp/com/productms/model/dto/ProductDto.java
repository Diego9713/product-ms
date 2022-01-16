package bootcamp.com.productms.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

  private String id;
  private String accountType;
  private String accountNumber;
  private String currency;
  private double amount = 0;
  private double maintenanceCommission;
  private LocalDateTime maintenanceCommissionDay;
  private int maxTransactNumber = 10;
  private LocalDateTime transactNumberDay;
  private double creditLimit;
  private String customer;
  private String status;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDate updateAt;
  private String updateBy;
  private double minimumAverageAmount = 0;
  private double averageDailyBalance = 0;
  private LocalDate averageDailyBalanceDay;
}



