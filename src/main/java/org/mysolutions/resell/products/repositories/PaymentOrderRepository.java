package org.mysolutions.resell.products.repositories;

import com.razorpay.Order;
import org.mysolutions.resell.products.entities.PaymentOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentOrderRepository extends MongoRepository<PaymentOrder, String> {
    PaymentOrder findByRazorpayOrderId(String razorpayOrderId);
}
