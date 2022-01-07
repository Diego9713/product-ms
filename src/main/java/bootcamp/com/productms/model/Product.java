package bootcamp.com.productms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
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
    private Date createdAt;
    @Field(name = "created_by")
    private String createdBy;
    @Field(name = "update_at")
    private Date updateAt;
    @Field(name = "update_by")
    private String updateBy;
    @Field(name = "currency")
    private String currency;
    @Field(name = "amount")
    private double amount;
    @Field(name = "maintenance_commission")
    private double maintenanceCommission;
    @Field(name = "max_transact_number")
    private int maxTransactNumber;
    @Field(name = "transact_number_day")
    private Date transactNumberDay;
    @Field(name = "credit_limit")
    private double creditLimit;
    @Field(name = "customer")
    private String customer;
    @Field(name = "status")
    private String status;
}
