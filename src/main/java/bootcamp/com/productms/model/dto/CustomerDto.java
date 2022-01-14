package bootcamp.com.productms.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {
  private String id;
  private String documentType;
  private String documentNumber;
  private String customerType;
  private String firstName;
  private String lastName;
  private String address;
  private String references;
  private String phoneNumber;
  private String civilStatus;
  private String email;
  private boolean owner;
  private String status;
}
