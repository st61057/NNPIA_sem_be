package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReservationResponseDto {

    @NotNull
    private String email;

    @NotNull
    private TimeSlotDto timeSlotDto;

    @NotNull
    private Date date;

    @NotNull
    private ProcedureDto procedureDto;
}
