package org.mysolutions.resell.products.controller;

import org.mysolutions.resell.products.entities.CustomerAddress;
import org.mysolutions.resell.products.repositories.CustomerAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerAddressController {

    @Autowired
    private CustomerAddressRepository repository;

    @PostMapping("/address")
    public CustomerAddress saveAddress(@RequestBody CustomerAddress address) {
        return repository.save(address);
    }

    @GetMapping("/{phone}")
    public java.util.List<CustomerAddress> getAddresses(@PathVariable String phone) {
        return repository.findAll().stream()
                .filter(addr -> addr.getPhone().equals(phone))
                .toList();
    }
}
