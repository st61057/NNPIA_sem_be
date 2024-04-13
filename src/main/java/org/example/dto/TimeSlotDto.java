package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDto {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private boolean isSlotFree;

    public TimeSlotDto(LocalTime startTime, LocalTime endTime, boolean isSlotFree) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSlotFree = isSlotFree;
    }
}
