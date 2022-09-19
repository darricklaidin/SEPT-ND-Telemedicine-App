package com.sept.authmicroservice.repository;

import com.sept.authmicroservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    User getByUserID(int id);

    Boolean existsByEmail(String email);
}