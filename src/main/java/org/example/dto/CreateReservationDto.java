package org.example.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class CreateReservationDto {

    private String email;

    private ProcedureDto procedure;

    private Date reservationDate;

    private TimeSlotDto time;

    private Timestamp createdDate;

}
