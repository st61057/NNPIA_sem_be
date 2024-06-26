package org.example.service;

import org.example.dto.TimeSlotDto;
import org.example.entity.Barbershop;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.repository.BarbershopRepository;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BarbershopService {

    private BarbershopRepository barbershopRepository;

    private ReservationRepository reservationRepository;

    public BarbershopService(BarbershopRepository barbershopRepository, ReservationRepository reservationRepository) {
        this.barbershopRepository = barbershopRepository;
        this.reservationRepository = reservationRepository;
    }

    public Barbershop findById(Integer id) {
        return barbershopRepository.findBarbershopById(id);
    }

    public List<TimeSlotDto> getTimeSlotsForDate(Date date, Integer barbershopId) {
        List<TimeSlotDto> timeSlots = new ArrayList<>();
        Barbershop barbershop = findById(barbershopId);
        LocalDateTime localDateTime = dateToLocaleDateTime(date);
        if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY || localDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return new ArrayList<>();
        }
        List<Reservation> reservations = reservationRepository.findReservationsByReservationDateAndStatus(date, ReservationStatus.CONFIRMED);
        reservations.addAll(reservationRepository.findReservationsByReservationDateAndStatus(date, ReservationStatus.CREATED));
        reservations.addAll(reservationRepository.findReservationsByReservationDateAndStatus(date, ReservationStatus.LOCKED));
        LocalTime openingTime = barbershop.getOpeningTime();
        LocalTime closingTime = barbershop.getClosingTime();

        while (openingTime.getHour() < closingTime.getHour()) {
            boolean freeSlot = true;
            for (Reservation reservation : reservations) {
                if ((!openingTime.isBefore(reservation.getStartTime()) || !openingTime.plusHours(1).isBefore(reservation.getStartTime().plusMinutes(1))) &&
                        (!openingTime.isAfter(reservation.getEndTime().minusMinutes(1)) || !openingTime.plusHours(1).isAfter(reservation.getEndTime()))) {
                    freeSlot = false;
                    break;
                }
            }
            timeSlots.add(new TimeSlotDto(openingTime, openingTime.plusHours(1), freeSlot));
            openingTime = openingTime.plusHours(1);
        }

        return timeSlots;
    }

    private LocalDateTime dateToLocaleDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


}
