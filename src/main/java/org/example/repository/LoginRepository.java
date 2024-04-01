package org.example.repository;

import org.example.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Integer> {

    Login findByUserName(String username);

}
