package org.mysolutions.resell.products.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mysolutions.resell.products.entities.Product;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String id;
    private List<Product> items;
    private double total;
    private LocalDateTime timestamp;
}
