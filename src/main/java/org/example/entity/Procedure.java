package org.example.entity;

import lombok.*;
import org.example.enums.RESERVATION_VALIDITY;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Procedure {

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
    @Column
    private RESERVATION_VALIDITY status;

}
