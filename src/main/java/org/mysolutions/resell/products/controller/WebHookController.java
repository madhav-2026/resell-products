package org.mysolutions.resell.products.controller;

import org.json.JSONObject;
import org.mysolutions.resell.products.entities.PaymentOrder;
import org.mysolutions.resell.products.repositories.PaymentOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RequestMapping("/api/payment")
public class WebHookController {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @PostMapping("/webhook")
    public ResponseEntity<?> razorpayWebhook(@RequestBody String payload,
                                             @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // Razorpay webhook secret (set in Razorpay dashboard)
            String webhookSecret = "mKYQB@4BpZLcPyB";

            // Verify signature
            String actualSignature = calculateRazorpaySignature(payload, webhookSecret);
            if (!actualSignature.equals(signature)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
            }

            // ...existing webhook logic...
            JSONObject webhookData = new JSONObject(payload);
            String event = webhookData.optString("event");
            if ("payment.captured".equals(event)) {
                JSONObject paymentEntity = webhookData.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                String razorpayOrderId = paymentEntity.optString("order_id");
                String paymentId = paymentEntity.optString("id");

                PaymentOrder order = paymentOrderRepository.findByRazorpayOrderId(razorpayOrderId);
                if (order != null) {
                    order.setStatus("Completed");
                    order.setPaymentId(paymentId);
                    paymentOrderRepository.save(order);
                }
            }
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // Utility method to verify Razorpay webhook signature
    private String calculateRazorpaySignature(String payload, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
        return new String(Base64.getEncoder().encode(hash));
    }

}
