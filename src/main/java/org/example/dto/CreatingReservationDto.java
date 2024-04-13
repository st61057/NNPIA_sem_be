package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreatingReservationDto {
    private String email;
    private ProcedureDto procedure;
    private Date reservationDate;
    private TimeSlotDto time;

}
