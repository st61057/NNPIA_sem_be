package org.example.repository;

import org.example.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<UserLogin, Integer> {

    UserLogin findByUsername(String username);

}
