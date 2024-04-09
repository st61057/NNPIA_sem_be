package org.example.entity;

import lombok.*;
import org.example.enums.ReservationValidity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Procedure {

    public Procedure(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String description;

    @NotNull
    @Column
    private Integer price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) default 'INACTIVE'")
    private ReservationValidity status;

}
