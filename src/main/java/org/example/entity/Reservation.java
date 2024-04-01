package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.RESERVATION_STATUS;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private RESERVATION_STATUS status;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;
    @NotNull
    @Column
    private LocalDateTime start;

    @NotNull
    @Column
    private LocalDateTime end;

}
