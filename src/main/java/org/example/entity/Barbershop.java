package org.example.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "barbershop")
public class Barbershop {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String name;

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    private Set<User> user;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<Procedure> procedures;
}
