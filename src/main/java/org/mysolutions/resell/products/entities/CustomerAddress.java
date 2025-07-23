package org.mysolutions.resell.products.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customer_addresses")
public class CustomerAddress {
    @Id
    private String id;
    private String phone;
    private String address;
    private String pincode;
    private double lat;
    private double lng;
}
