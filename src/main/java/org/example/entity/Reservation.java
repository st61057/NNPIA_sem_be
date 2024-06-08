package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ReservationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @NotNull
    @Column
    @Temporal(TemporalType.DATE)
    private Date reservationDate;

    @NotNull
    @Column
    private LocalTime startTime;

    @NotNull
    @Column
    private LocalTime endTime;

    @NotNull
    @Column
    private Timestamp createdTime;
}
