package com.mkohan.render.repositories;

import com.mkohan.render.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional(readOnly = true)
    Optional<User> findByUsername(String username);
}
