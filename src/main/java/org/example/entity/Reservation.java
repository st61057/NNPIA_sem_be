package org.example.entity;

import org.example.enums.RESERVATION_STATUS;

import java.time.LocalDateTime;

public class Reservation {

    private Integer id;

    private String email;

    private RESERVATION_STATUS status;

    private Procedure procedure;

    private LocalDateTime start;

    private LocalDateTime end;

}
