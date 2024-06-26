package org.example.repository;

import org.example.entity.Barbershop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbershopRepository extends JpaRepository<Barbershop, Integer> {
    Barbershop findBarbershopById(Integer id);
}
