package org.example.repository;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findReservationById(Integer id);

    List<Reservation> findReservationsByEmail(String email);

    List<Reservation> findReservationsByProcedureId(Integer procedureId);

    List<Reservation> findReservationsByReservationDate(Date date);

    List<Reservation> findReservationsByReservationDateAndStatus(Date date, ReservationStatus status);

    Reservation findReservationByStartTimeBetweenAndEndTime(Date date, LocalTime startTime, LocalTime endTime);

}
