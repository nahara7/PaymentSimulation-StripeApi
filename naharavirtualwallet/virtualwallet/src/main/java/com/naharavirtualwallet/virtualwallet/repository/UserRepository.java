package com.naharavirtualwallet.virtualwallet.repository;

import com.naharavirtualwallet.virtualwallet.model.User;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableRedisRepositories
public interface UserRepository extends CrudRepository<User, String>{

    User save(User user);
    Optional<User> findById(String id);
    void update(User user);
    void deleteById(String id);

}
