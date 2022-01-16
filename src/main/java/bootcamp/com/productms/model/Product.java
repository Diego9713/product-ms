package bootcamp.com.productms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document("product")
public class Product {
  @Id
  private String id;
  @Field(name = "account_type")
  private String accountType;
  @Field(name = "account_number")
  private String accountNumber;
  @Field(name = "created_at")
  private LocalDateTime createdAt;
  @Field(name = "created_by")
  private String createdBy;
  @Field(name = "update_at")
  private LocalDate updateAt;
  @Field(name = "update_by")
  private String updateBy;
  @Field(name = "currency")
  private String currency;
  @Field(name = "amount")
  private double amount = 0;
  @Field(name = "minimum_average_amount")
  private double minimumAverageAmount = 0;
  @Field(name = "average_daily_balance")
  private double averageDailyBalance = 0;
  @Field(name = "average_daily_balance_day")
  private LocalDate averageDailyBalanceDay;
  @Field(name = "maintenance_commission")
  private double maintenanceCommission;
  @Field(name = "maintenance_commission_day")
  private LocalDateTime maintenanceCommissionDay;
  @Field(name = "max_transact_number")
  private int maxTransactNumber = 10;
  @Field(name = "transact_number_day")
  private LocalDateTime transactNumberDay;
  @Field(name = "credit_limit")
  private double creditLimit;
  @Field(name = "customer")
  private String customer;
  @Field(name = "status")
  private String status;
}
