package org.mysolutions.resell.products.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private String id;
    private String customerPhone;
    private String customerName;
    private Date date;
    private List<Product> items;
    private Double total;
    private String status;
    private Double deliveryCharge;
    private Double latitude;
    private Double longitude;
    private String address;
    private String razorpayOrderId;
    private String paymentId;
}
