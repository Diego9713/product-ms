package bootcamp.com.productms.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCustomerDto {
  private String id;
  private String accountType;
  private String accountNumber;
  private String currency;
  private double amount;
  private double creditLimit;
  private String status;
}
