package org.mysolutions.resell.products.entities;

import lombok.Builder;
import lombok.Data;
import org.mysolutions.resell.products.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "customer_addresses")
@Builder
public class CustomerAddress {
    @Id
    private String id;
    private String phone;
    private List<Address> addresses;
}
