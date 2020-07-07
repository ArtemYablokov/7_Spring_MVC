package com.yabloko.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yabloko.models.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    List<User> findAllByFirstName(String firstName);
}