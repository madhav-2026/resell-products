package org.mysolutions.resell.products.controller;

import org.mysolutions.resell.products.entities.Order;
import org.mysolutions.resell.products.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String ORDER_PENDING = "Pending";
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        order.setDate(new Date());
        order.setStatus(ORDER_PENDING);
        return orderRepository.save(order);
    }

    @GetMapping("/history/{phone}")
    public List<Order> getOrderHistory(@PathVariable String phone) {
        return orderRepository.findByUserPhoneOrderByDateDesc(phone);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(@RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("All")) {
            return orderRepository.findByStatusOrderByDateDesc(status);
        } else {
            return orderRepository.findAllByOrderByDateDesc();
        }
    }

    // Update order status
    @PatchMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable String id, @RequestBody StatusUpdate statusUpdate) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(statusUpdate.status);
        return orderRepository.save(order);
    }

    // DTO for status update
    public static class StatusUpdate {
        public String status;
    }
}
