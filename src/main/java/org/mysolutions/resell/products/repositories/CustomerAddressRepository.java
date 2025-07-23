package org.mysolutions.resell.products.repositories;

import org.mysolutions.resell.products.entities.CustomerAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerAddressRepository extends MongoRepository<CustomerAddress, String> {
}
