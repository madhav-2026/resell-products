package org.mysolutions.resell.products.repositories;

import org.mysolutions.resell.products.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByOrderByOrderDateDesc();
}

