package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Reservation;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ReservationPagingDto {

    private List<Reservation> reservationsList = new ArrayList<>();

    private Integer numberOfReservations = 0;
}
