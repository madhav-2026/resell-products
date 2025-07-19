package org.mysolutions.resell.products.repositories;

import org.mysolutions.resell.products.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Users, String> {

    Optional<Users> findByPhone(String phone);

}
