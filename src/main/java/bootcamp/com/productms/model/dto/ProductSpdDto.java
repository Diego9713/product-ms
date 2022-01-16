package bootcamp.com.productms.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSpdDto {
  private String customer;
  private String accountNumber;
  private double minimumAverageAmount;
  private String status;

}
