package org.example.repository;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findReservationById(Integer id);

    List<Reservation> findReservationsByEmail(String email);

    List<Reservation> findReservationsByProcedureId(Integer procedureId);

    List<Reservation> findReservationsByReservationDateAndStatus(Date date, ReservationStatus status);

    List<Reservation> findReservationsByReservationDate(Date date);

    List<Reservation> findReservationsByCreatedTimeAndStatus(Timestamp timestamp, ReservationStatus status);
    List<Reservation> findReservationsByStatusAndCreatedTimeBefore(ReservationStatus status, Timestamp timestamp);

}
