package org.example.repository;

import org.example.entity.Procedure;
import org.example.enums.ProcedureValidity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {

    Procedure findByName(String name);

    List<Procedure> findAll();

    List<Procedure> findAllByStatus(ProcedureValidity validity);


}
