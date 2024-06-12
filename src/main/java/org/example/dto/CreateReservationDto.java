package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class CreateReservationDto {

    @NotNull
    private String email;

    @NotNull
    private ProcedureDto procedure;

    @NotNull
    private Date reservationDate;

    @NotNull
    private TimeSlotDto time;

    @NotNull
    private Timestamp createdDate;

}
