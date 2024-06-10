package org.example;

import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.ProcedureValidity;
import org.example.enums.ReservationStatus;
import org.example.repository.ProcedureRepository;
import org.example.repository.ReservationRepository;
import org.example.service.ProcedureService;
import org.example.service.ReservationService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Tests {

    @Autowired
    ProcedureRepository procedureRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ProcedureService procedureService;

    @Autowired
    ReservationService reservationService;

    Procedure procedure;
    Reservation reservation;

    @BeforeEach
    public void setUp() throws Exception {
        procedure = new Procedure();
        procedure.setName("TestProcedure");
        procedure.setDescription("TestProcedure");
        procedure.setPrice(400);
        procedure.setStatus(ProcedureValidity.ACTIVE);
        if (procedureRepository.findByName("TestProcedure") == null) {
            procedureRepository.save(procedure);
        }

        reservation = new Reservation();
        reservation.setId(0);
        reservation.setReservationDate(new Date());
        reservation.setStartTime(LocalTime.of(8, 0, 0));
        reservation.setEndTime(LocalTime.of(9, 0, 0));
        reservation.setEmail("test@test.com");
        reservation.setStatus(ReservationStatus.CREATED);
        reservation.setProcedure(procedureService.findByName("TestProcedure"));
        reservation.setCreatedTime(Timestamp.from(Instant.now()));
        reservationService.createReservation(reservation);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        List<Reservation> existingReservation = reservationRepository.findReservationsByProcedureId(procedureService.findByName("TestProcedure").getId());
        reservationService.deleteReservation(existingReservation.get(0));
        Procedure existingProcedure = procedureService.findByName("TestProcedure");
        procedureService.deleteProcedure(existingProcedure.getId());
    }

    @Test
    void updateExistingProcedurePrice() throws Exception {
        ProcedureDto procedureDto = new ProcedureDto();
        procedureDto.setId(procedure.getId());
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(500);
        procedureDto.setDescription(procedure.getDescription());
        procedureDto.setChecked(procedure.getStatus().equals(ProcedureValidity.ACTIVE) ? true : false);
        procedureService.updateProcedure(procedureDto);

        Procedure updatedProcedure = procedureService.findByName("TestProcedure");
        Assert.assertNotEquals(procedure.getPrice(), updatedProcedure.getPrice());
    }

    @Test
    void changeStatusToExistingReservation() throws Exception {

        List<Reservation> procedureList = reservationRepository.findReservationsByProcedureId(procedureService.findByName("TestProcedure").getId());
        reservationService.confirmReservation(procedureList.get(0).getId());
        Assert.assertEquals(procedureList.get(0).getStatus(), ReservationStatus.CREATED);
    }
}
