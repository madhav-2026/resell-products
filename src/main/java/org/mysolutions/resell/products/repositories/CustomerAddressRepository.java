package org.mysolutions.resell.products.repositories;

import org.mysolutions.resell.products.entities.CustomerAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerAddressRepository extends MongoRepository<CustomerAddress, String> {
    Optional<CustomerAddress> findByPhone(String phone);
}
