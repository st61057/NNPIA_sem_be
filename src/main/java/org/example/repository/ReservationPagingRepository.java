package org.example.repository;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface ReservationPagingRepository extends PagingAndSortingRepository<Reservation, Integer> {
    Page<Reservation> findAllByEmailAndReservationDateAndStatus(String email, Date reservationDate, ReservationStatus status, Pageable pageVariable);

    Page<Reservation> findAllByStatus(ReservationStatus status, Pageable pageVariable);

    Page<Reservation> findAllByStatusAndReservationDate(Date reservationDate, ReservationStatus status, Pageable pageVariable);

}
