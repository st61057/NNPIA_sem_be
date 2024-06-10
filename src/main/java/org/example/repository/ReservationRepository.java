package org.example.repository;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findReservationById(Integer id);

    List<Reservation> findReservationsByProcedureId(Integer procedureId);

    List<Reservation> findReservationsByReservationDateAndStatus(Date date, ReservationStatus status);

    List<Reservation> findReservationsByReservationDate(Date date);

    Reservation findReservationByReservationDateAndStartTimeAndEndTime(Date date, LocalTime startTime, LocalTime endTime);

    List<Reservation> findReservationsByStatusAndCreatedTimeBefore(ReservationStatus status, Timestamp timestamp);

}
