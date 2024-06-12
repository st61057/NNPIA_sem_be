package org.example.service;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.repository.ReservationPagingRepository;
import org.example.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    private ReservationPagingRepository reservationPagingRepository;

    private CleanService cleanService;

    public ReservationService(ReservationRepository reservationRepository, ReservationPagingRepository reservationPagingRepository, CleanService cleanService) {
        this.reservationRepository = reservationRepository;
        this.reservationPagingRepository = reservationPagingRepository;
        this.cleanService = cleanService;
    }

    public Reservation createReservation(Reservation reservation) throws Exception {
        List<Reservation> existingReservations = reservationRepository.findReservationsByReservationDate(reservation.getReservationDate());
        if (checkAvailability(reservation, existingReservations)) {
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new Exception("This time slot is currently locked try again in several minutes");
    }

    public Reservation updateReservation(Reservation reservation) throws Exception {
        Reservation existingReservation = reservationRepository.findReservationByReservationDateAndStartTimeAndEndTime(reservation.getReservationDate(), reservation.getStartTime(), reservation.getEndTime());
        if (existingReservation != null) {
            cleanService.cleanLockedReservation(existingReservation.getId());
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new Exception("This reservation doesn't exist");
    }


    public Reservation deleteReservation(Reservation reservation) throws Exception {
        Reservation existingReservation = reservationRepository.findReservationByReservationDateAndStartTimeAndEndTime(reservation.getReservationDate(), reservation.getStartTime(), reservation.getEndTime());
        if (existingReservation != null) {
            reservationRepository.delete(existingReservation);
            return reservation;
        }
        throw new Exception("This reservation doesn't exist");
    }

    public Reservation deleteReservation(Integer id) throws Exception {
        Reservation existingReservation = findById(id);
        if (existingReservation != null) {
            reservationRepository.delete(existingReservation);
            return existingReservation;
        }
        throw new Exception("This reservation doesn't exist");
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

    public Page<Reservation> findAllByReservationDateAndStatus(Date reservationDate, ReservationStatus status, Pageable pageVariable) {
        return reservationPagingRepository.findAllByReservationDateAndStatus(reservationDate, status, pageVariable);
    }

    private Reservation findById(Integer id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get();
        } else {
            throw new NoSuchElementException("Reservation with id " + id + " was not found!");
        }
    }

    private boolean checkAvailability(Reservation reservation, List<Reservation> reservations) {
        boolean canBeCreated = true;
        for (Reservation reservationFromDb : reservations) {
            if (!(reservation.getStartTime().plusMinutes(1).isBefore(reservationFromDb.getStartTime()) && reservation.getEndTime().minusMinutes(1).isBefore(reservationFromDb.getStartTime())) &&
                    !(reservation.getStartTime().plusMinutes(1).isAfter(reservationFromDb.getEndTime()) && reservation.getEndTime().minusMinutes(1).isAfter(reservationFromDb.getEndTime()))) {
                canBeCreated = false;
                break;
            }
        }
        return canBeCreated;
    }
}
