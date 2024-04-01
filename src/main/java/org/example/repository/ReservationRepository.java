package org.example.repository;

import org.example.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findReservationsByEmail(String email);

    List<Reservation> findReservationsByProcedureId(Integer procedureId);
}
