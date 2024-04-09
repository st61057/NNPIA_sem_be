package org.example.repository;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findReservationById(Integer id);

    List<Reservation> findReservationsByEmail(String email);

    List<Reservation> findReservationsByProcedureId(Integer procedureId);

    List<Reservation> findReservationsByDate(Date date);

    List<Reservation> findReservationsByDateAndStatus(Date date, ReservationStatus status);

    Reservation findReservationByStartBetweenAndEnd(Date date, LocalDateTime start, LocalDateTime end);

}
