package com.yabloko.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yabloko.models.User;

import java.util.List;

/**
 * 18.04.2018
 * UsersRepository
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface UsersRepository extends JpaRepository<User, Long> {
    List<User> findAllByFirstName(String firstName);
}
