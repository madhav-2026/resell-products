package org.mysolutions.resell.products.controller;

import org.mysolutions.resell.products.entities.CustomerAddress;
import org.mysolutions.resell.products.model.Address;
import org.mysolutions.resell.products.repositories.CustomerAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerAddressController {

    @Autowired
    private CustomerAddressRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/address")
    public CustomerAddress saveAddress(@RequestParam String phone,
                                       @RequestBody Address reqAddress) {
        Optional<CustomerAddress> customerAddressOptional =
                repository.findByPhone(phone);
        reqAddress.set_id(UUID.randomUUID().toString());
        if(customerAddressOptional.isPresent()) {
            return updateAddress(phone, reqAddress);
        } else {
            CustomerAddress customerAddress = CustomerAddress.builder()
                    .addresses(Collections.singletonList(reqAddress))
                    .phone(phone).build();
            return repository.save(customerAddress);
        }
    }

    @GetMapping("/{phone}")
    public CustomerAddress getAddresses(@PathVariable String phone) {
        return repository.findByPhone(phone).get();
    }

    public CustomerAddress updateAddress(String phone, Address newAddress) {
        Query query = new Query(where("phone").is(phone));
        Update update = new Update().push("addresses", newAddress); // To add a new address
        return mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                CustomerAddress.class
        );
    }
}
