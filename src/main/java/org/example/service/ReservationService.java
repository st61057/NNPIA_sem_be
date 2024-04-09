package org.example.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) {
        Reservation existingReservation = reservationRepository.findReservationByStartBetweenAndEnd(reservation.getReservationDate(), reservation.getStart(), reservation.getEnd());
        if (existingReservation == null) {
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new RuntimeException("This time slot is already reserved");
    }

    public Reservation confirmReservation(Integer id) {
        Reservation reservation = reservationRepository.findReservationById(id);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }
    public Reservation cancelReservation(Integer id) {
        Reservation reservation = reservationRepository.findReservationById(id);
        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }
}
