package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.*;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.entity.UserLogin;
import org.example.enums.ReservationStatus;
import org.example.service.BarbershopService;
import org.example.service.ProcedureService;
import org.example.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping()
@AllArgsConstructor
@NoArgsConstructor
public class ReservationController {

    private BarbershopService beautySalonService;

    private ProcedureService procedureService;

    private ReservationService reservationService;

    private ModelMapper modelMapper;

    @GetMapping("/public/reservation")
    public List<TimeSlotDto> getAll(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Long id, @AuthenticationPrincipal UserLogin userLogin) {
        return beautySalonService.getTimeSlotsForDate(date, id);
    }

    @PostMapping("/public/reservation")
    public ResponseEntity<?> createReservation(@RequestBody @Valid CreateReservationDto createReservationDtoIn, @AuthenticationPrincipal UserLogin userLogin) {
        try {
            Reservation reservation = convertToEntity(createReservationDtoIn);
            ReservationResponseDto reservationCreated = convertToReservationDto(reservationService.createReservation(reservation));
            return ResponseEntity.status(200).body(reservationCreated);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Create reservation failed, maybe this time is already booked.");
        }
    }

    @PutMapping("/api/reservation/confirm")
    public ResponseEntity<?> confirmReservation(@RequestBody Integer resId, @AuthenticationPrincipal UserLogin userLogin) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.confirmReservation(resId));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Confirm reservation failed.");
        }
    }

    @PutMapping("/api/reservation/asDone")
    public ResponseEntity<?> setAsDone(@RequestBody Integer resId, @AuthenticationPrincipal UserLogin userLogin) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.setAsDone(resId));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Setting reservation as done failed.");
        }
    }

    @PutMapping("/api/reservation/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody Integer cancelDtoIn, @AuthenticationPrincipal UserLogin userLogin) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.cancelReservation(cancelDtoIn));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Cancel reservation failed.");
        }

    }

    @GetMapping("/api/reservation/by-email-date-and-status")
    public ResponseEntity<?> getAllByEmailAndDateAndStatus(String email, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult = reservationService.findAllByEmailAndReservationDateAndStatus(email, date, status, pageable);
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }

    @GetMapping("/api/reservation/by-date-and-status")
    public ResponseEntity<?> getAllByDateAndStatus(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult = reservationService.findAllByStatusAndReservationDate(date, status, pageable);
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }

    @GetMapping("/api/reservation/by-status")
    public ResponseEntity<?> getAllByStatus(ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult = reservationService.findAllByStatus(status, pageable);
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }


    private Reservation convertToEntity(CreateReservationDto createReservationDtoIn) {
        Reservation reservation = new Reservation();
        reservation.setEmail(createReservationDtoIn.getEmail());
        reservation.setReservationDate(createReservationDtoIn.getReservationDate());
        reservation.setStartTime(createReservationDtoIn.getTime().getStartTime());
        reservation.setEndTime(createReservationDtoIn.getTime().getEndTime());
        reservation.setStatus(ReservationStatus.CREATED);
        reservation.setProcedure(procedureService.findByName(createReservationDtoIn.getProcedure().getName()));
        return reservation;
    }

    private ReservationResponseDto convertToReservationDto(Reservation reservation) {
        ReservationResponseDto reservationDtoOut = new ReservationResponseDto();
        reservationDtoOut.setDate(reservation.getReservationDate());
        reservationDtoOut.setEmail(reservationDtoOut.getEmail());
        reservationDtoOut.setTimeSlotDto(new TimeSlotDto(reservation.getStartTime(), reservation.getEndTime(), true));
        reservationDtoOut.setProcedureDto(convertProcedureToDto(reservation.getProcedure()));
        return reservationDtoOut;
    }

    private ReservationPagingDto convertToPagingDto(Page<Reservation> pagedResult) {
        if (pagedResult.hasContent()) {
            ReservationPagingDto reservationPagingDto = new ReservationPagingDto();
            reservationPagingDto.setReservationsList(pagedResult.getContent());
            reservationPagingDto.setNumberOfReservations(pagedResult.getNumberOfElements());
            return reservationPagingDto;
        } else {
            return new ReservationPagingDto();
        }
    }

    public ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(procedure.getPrice());
        procedureDto.setDescription(procedure.getDescription());
        return procedureDto;
    }

}
