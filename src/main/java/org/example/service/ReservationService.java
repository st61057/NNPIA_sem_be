package org.example.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.repository.ReservationPagingRepository;
import org.example.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationPagingRepository reservationPagingRepository;

    public Reservation createReservation(Reservation reservation) {
        Reservation existingReservation = reservationRepository.findReservationByStartTimeBetweenAndEndTime(reservation.getReservationDate(), reservation.getStartTime(), reservation.getEndTime());
        if (existingReservation == null) {
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new RuntimeException("This time slot is already reserved");
    }

    public Reservation confirmReservation(Integer id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Integer id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }

    public Reservation setAsDone(Integer id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.DONE);
        return reservationRepository.save(reservation);
    }

    public Page<Reservation> findAllByEmailAndReservationDateAndStatus(String email, Date reservationDate, ReservationStatus status, Pageable pageVariable) {
        return reservationPagingRepository.findAllByEmailAndReservationDateAndStatus(email, reservationDate, status, pageVariable);
    }

    public Page<Reservation> findAllByStatus(ReservationStatus status, Pageable pageVariable) {
        return reservationPagingRepository.findAllByStatus(status, pageVariable);
    }

    public Page<Reservation> findAllByStatusAAndReservationDate(Date reservationDate, ReservationStatus status, Pageable pageVariable) {
        return reservationPagingRepository.findAllByStatusAAndReservationDate(reservationDate, status, pageVariable);
    }

    private Reservation findById(Integer id){
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get();
        } else {
            throw new NoSuchElementException("Reservation with id " + id + " was not found!");
        }
    }
}
