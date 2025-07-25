package org.mysolutions.resell.products.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.mysolutions.resell.products.entities.PaymentOrder;
import org.mysolutions.resell.products.repositories.OrderRepository;
import org.mysolutions.resell.products.repositories.PaymentOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        try {
            int amount = (int) payload.get("amount"); // in INR
            RazorpayClient razorpay = new RazorpayClient("rzp_test_bmXXAclygUWgTk", "CtAJIqBwO8Y7lgYuyzG9MBFf");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> razorpayWebhook(@RequestBody String payload,
                                             @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // Optionally, verify signature here using your Razorpay secret
            JSONObject webhookData = new JSONObject(payload);
            String event = webhookData.optString("event");
            if ("payment.captured".equals(event)) {
                JSONObject paymentEntity = webhookData.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                String razorpayOrderId = paymentEntity.optString("order_id");
                String paymentId = paymentEntity.optString("id");

                // Find your order in MongoDB using razorpayOrderId
                org.mysolutions.resell.products.entities.Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId);
                if (order != null) {
                    order.setStatus("Completed");
                    order.setPaymentId(paymentId);
                    org.mysolutions.resell.products.entities.Order save = orderRepository.save(order);
                }
            }
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/save-payment")
    public ResponseEntity<?> savePayment(@RequestBody Map<String, Object> payload) {
        try {
            String razorpayPaymentId = (String) payload.get("paymentId");
            String razorpayOrderId = (String) payload.get("razorpayOrderId");
            String status = (String) payload.get("status"); // e.g., "Completed"
            // Find order by Razorpay order ID
            org.mysolutions.resell.products.entities.Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId);
            if (order != null) {
                order.setPaymentId(razorpayPaymentId);
                order.setStatus(status);
                order.setRazorpayOrderId(razorpayOrderId);
                org.mysolutions.resell.products.entities.Order save = orderRepository.save(order);
                return ResponseEntity.ok("Payment info saved");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-razorpay-id")
    public ResponseEntity<?> updateRazorpayOrderId(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId"); // Your MongoDB order _id
        String razorpayOrderId = payload.get("razorpayOrderId");
        org.mysolutions.resell.products.entities.Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setRazorpayOrderId(razorpayOrderId);
            orderRepository.save(order);
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
    }
}
