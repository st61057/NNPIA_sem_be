package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ReservationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @NotNull
    @Column
    private Date reservationDate;

    @NotNull
    @Column
    private LocalDateTime start;

    @NotNull
    @Column
    private LocalDateTime end;

}
