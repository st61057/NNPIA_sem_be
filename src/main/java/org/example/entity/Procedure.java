package org.example.entity;

import lombok.*;
import org.example.enums.ReservationValidity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Procedure {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column
    private String description;

    @NotNull
    @Column
    private Integer price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) default 'INVALID'")
    private ReservationValidity status;

}
