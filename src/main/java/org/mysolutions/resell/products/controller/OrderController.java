package org.mysolutions.resell.products.controller;

import com.twilio.Twilio;
import org.mysolutions.resell.products.entities.Order;
import org.mysolutions.resell.products.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.twilio.rest.api.v2010.account.Message;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Value("${twilio.whatsapp.to}")
    private String toNumber;

    private static final String ORDER_PENDING = "Pending";
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        order.setDate(new Date());
        order.setStatus(ORDER_PENDING);
        Order savedOrder = orderRepository.save(order);

        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + order.getLatitude() + "," +
                order.getLongitude();

        StringBuilder sb = new StringBuilder();
        sb.append("New Order Received:\n");
        order.getItems().forEach(item -> sb.append(item.getName())
                .append(" x ").append(item.getQuantity())
                .append(" = ₹").append(item.getPrice()).append("\n"));
        sb.append("\n");
        sb.append("Total: ₹").append(order.getTotal()).append("\n");
        sb.append("Delivery Charges: ₹").append(order.getDeliveryCharge()).append("\n");
        sb.append("Grand Total: ₹").append(order.getTotal() + order.getDeliveryCharge()).append("\n");
        sb.append("\n");
        sb.append("Customer Name: ").append(order.getCustomerName()).append("\n");
        sb.append("Contact Number: ").append(order.getCustomerPhone()).append("\n");
        sb.append("Address:").append(order.getAddress()).append("\n");
        sb.append("Location:").append(googleMapsUrl).append("\n");


        // Send WhatsApp message via Twilio
//        Twilio.init(accountSid, authToken);
//        Message.creator(
//                new com.twilio.type.PhoneNumber("whatsapp:" + toNumber),
//                new com.twilio.type.PhoneNumber("whatsapp:" + fromNumber),
//                sb.toString()
//        ).create();

//        //Acknoledgement
//        String customerWhatsapp = "whatsapp:" + order.getCustomerPhone(); // e.g., whatsapp:+919876543210
//        String ackMessage = "Thank you for your order!\nOrder ID: " + order.getId() +
//                "\nWe have received your order and will process it soon.\nTo cancel, please visit ATO app.";
//
//        Message.creator(
//                new com.twilio.type.PhoneNumber(customerWhatsapp),
//                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"), // Twilio sandbox sender
//                ackMessage).create();
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/history/{phone}")
    public List<Order> getOrderHistory(@PathVariable String phone) {
        return orderRepository.findByCustomerPhoneOrderByDateDesc(phone);
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
    @PatchMapping("/status/{id}")
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
