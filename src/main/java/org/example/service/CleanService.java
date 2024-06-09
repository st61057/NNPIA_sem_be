package org.example.service;

import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanService {

    private ReservationRepository reservationRepository;

    public CleanService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(fixedRate = 60000) //ms
    @Transactional
    public void cleanLockedOldRecords() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(1);
        List<Reservation> expiredReservations = reservationRepository.findReservationsByStatusAndCreatedTimeBefore(ReservationStatus.LOCKED, Timestamp.valueOf(fiveMinutesAgo));
        reservationRepository.deleteAll(expiredReservations);
    }

    public void cleanLockedReservation(Integer id) {
        Reservation expiredReservations = reservationRepository.findReservationById(id);
        reservationRepository.delete(expiredReservations);
    }
}
