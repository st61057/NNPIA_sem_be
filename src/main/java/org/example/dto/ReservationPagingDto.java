package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Reservation;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ReservationPagingDto {
    List<Reservation> reservationsList = new ArrayList<>();
    Integer numberOfReservations;
}
