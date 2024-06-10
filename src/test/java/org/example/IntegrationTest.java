package org.example;

import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.ProcedureValidity;
import org.example.enums.ReservationStatus;
import org.example.repository.*;
import org.example.service.ProcedureService;
import org.example.service.ReservationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    @Autowired
    ProcedureRepository procedureRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ProcedureService procedureService;

    @Autowired
    ReservationService reservationService;

    @AfterAll
    public void cleanUp() {
        Procedure testProcedure = procedureRepository.findByName("Test procedure");
        List<Reservation> reservationByBeautyProcedure = reservationRepository.findReservationsByProcedureId(testProcedure.getId());
        reservationRepository.deleteById(reservationByBeautyProcedure.get(0).getId());
        procedureRepository.deleteById(testProcedure.getId());
    }

    @Test
    void createReservationAfterCreateProcedure() throws Exception {
        Procedure procedure = new Procedure();
        procedure.setName("Test procedure");
        procedure.setDescription("Test procedure");
        procedure.setPrice(200);
        procedure.setStatus(ProcedureValidity.ACTIVE);

        Procedure procedureCreate = procedureService.createNewProcedure(procedure);
        Procedure newProcedure = procedureService.findByName("Test procedure");

        Reservation reservation = new Reservation();
        reservation.setReservationDate(new Date());
        reservation.setStartTime(LocalTime.of(8, 0, 0));
        reservation.setEndTime(LocalTime.of(9, 0, 0));
        reservation.setEmail("test@test.com");
        reservation.setStatus(ReservationStatus.CREATED);
        reservation.setProcedure(newProcedure);
        reservation.setCreatedTime(Timestamp.from(Instant.now()));

        Reservation reservationCreate = reservationService.createReservation(reservation);

        Assertions.assertEquals(procedureCreate, procedure);
        Assertions.assertEquals(reservationCreate, reservation);
    }

}
