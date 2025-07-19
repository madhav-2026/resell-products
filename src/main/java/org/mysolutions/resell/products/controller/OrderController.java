package org.mysolutions.resell.products.controller;

import org.mysolutions.resell.products.entities.Order;
import org.mysolutions.resell.products.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    @GetMapping("/get-order")
    public Order getOrder(String id) {
        return orderRepository.findById(id).get();
    }
    @DeleteMapping
    public void deleteOrderHistory() {
        orderRepository.deleteAll();
    }

}
