package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationResponseDto {

    private String email;

    private TimeSlotDto timeSlotDto;

    private Date date;

    private ProcedureDto procedureDto;
}
