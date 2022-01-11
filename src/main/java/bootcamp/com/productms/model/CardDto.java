package bootcamp.com.productms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private String id;
    private String productId;
    private String cardNumber;
    private String cardType;
    private String cvv;
    private int month;
    private int year;
    private String status;
}
