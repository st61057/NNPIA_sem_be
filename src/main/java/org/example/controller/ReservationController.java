package org.example.controller;

import org.example.dto.*;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.service.BarbershopService;
import org.example.service.ProcedureService;
import org.example.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping()
public class ReservationController {

    private final BarbershopService barbershopService;

    private final ProcedureService procedureService;

    private final ReservationService reservationService;

    private final ModelMapper modelMapper;

    public ReservationController(BarbershopService barbershopService, ProcedureService procedureService, ReservationService reservationService, ModelMapper modelMapper) {
        this.barbershopService = barbershopService;
        this.procedureService = procedureService;
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/public/reservation")
    public List<TimeSlotDto> getAll(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Integer id) {
        return barbershopService.getTimeSlotsForDate(date, id);
    }

    @PostMapping("/public/reservation")
    public ResponseEntity<?> updateReservation(@RequestBody @Valid CreateReservationDto createReservationDtoIn) {
        try {
            Reservation reservation = convertToEntity(createReservationDtoIn, ReservationStatus.CREATED);
            ReservationResponseDto reservationCreated = convertToReservationDto(reservationService.updateReservation(reservation));
            return ResponseEntity.status(200).body(reservationCreated);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Create reservation failed, maybe this time is already booked.");
        }
    }

    @PostMapping("/public/reservation-locked")
    public ResponseEntity<?> createLockedReservation(@RequestBody CreateReservationDto createReservationDtoIn) {
        try {
            createReservationDtoIn.setProcedure(convertProcedureToDto(procedureService.findById(0)));
            Reservation reservation = convertToEntity(createReservationDtoIn, ReservationStatus.LOCKED);
            ReservationResponseDto reservationCreated = convertToReservationDto(reservationService.createReservation(reservation));
            return ResponseEntity.status(200).body(reservationCreated);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

    @PostMapping("/public/reservation-locked-temp")
    public ResponseEntity<?> deleteLockedReservation(@RequestBody CreateReservationDto createReservationDtoIn) {
        try {
            createReservationDtoIn.setProcedure(convertProcedureToDto(procedureService.findById(0)));
            Reservation reservation = convertToEntity(createReservationDtoIn, ReservationStatus.LOCKED);
            ReservationResponseDto reservationCreated = convertToReservationDto(reservationService.deleteReservation(reservation));
            return ResponseEntity.status(200).body(reservationCreated);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

    @PutMapping("/api/reservation/confirm")
    public ResponseEntity<?> confirmReservation(@RequestBody Integer resId) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.confirmReservation(resId));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Confirm reservation failed.");
        }
    }

    @PutMapping("/api/reservation/asDone/{resId}")
    public ResponseEntity<?> setAsDone(@PathVariable Integer resId) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.setAsDone(resId));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Setting reservation as done failed.");
        }
    }

    @PutMapping("/api/reservation/cancel/{cancelDtoIn}")
    public ResponseEntity<?> cancelReservation(@PathVariable Integer cancelDtoIn) {
        try {
            ReservationResponseDto reservation = convertToReservationDto(reservationService.cancelReservation(cancelDtoIn));
            return ResponseEntity.status(200).body(reservation);
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Cancel reservation failed.");
        }
    }

    @GetMapping("/api/reservation/by-email-date-and-status")
    public ResponseEntity<?> getAllByEmailAndDateAndStatus(String email, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult = reservationService.findAllByEmailAndReservationDateAndStatus(email, date, status, pageable);
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }

    @GetMapping("/api/reservation/")
    public ResponseEntity<?> getAllByDateAndStatus(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult;
        if (status.equals(ReservationStatus.ALL)) {
            Page<Reservation> pageCreated = reservationService.findAllByReservationDateAndStatus(date, ReservationStatus.CREATED, pageable);
            Page<Reservation> pagedConfirmed = reservationService.findAllByReservationDateAndStatus(date, ReservationStatus.CONFIRMED, pageable);
            Page<Reservation> pagedDone = reservationService.findAllByReservationDateAndStatus(date, ReservationStatus.DONE, pageable);
            Page<Reservation> pagedCancelled = reservationService.findAllByReservationDateAndStatus(date, ReservationStatus.CANCELED, pageable);
            Page<Reservation> pagedLocked = reservationService.findAllByReservationDateAndStatus(date, ReservationStatus.LOCKED, pageable);
            List<Reservation> merged = mergePages(pageCreated, pagedConfirmed, pagedDone, pagedCancelled, pagedLocked);
            pagedResult = new PageImpl<>(merged, pageable, merged.size());
        } else {
            pagedResult = reservationService.findAllByReservationDateAndStatus(date, status, pageable);
        }
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }

    @GetMapping("/api/reservation/by-status")
    public ResponseEntity<?> getAllByStatus(ReservationStatus status, Pageable pageable) {
        Page<Reservation> pagedResult = reservationService.findAllByStatus(status, pageable);
        ReservationPagingDto reservationPagingDto = convertToPagingDto(pagedResult);
        return ResponseEntity.ok(reservationPagingDto);
    }


    private Reservation convertToEntity(CreateReservationDto createReservationDtoIn, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setEmail(createReservationDtoIn.getEmail());
        reservation.setReservationDate(createReservationDtoIn.getReservationDate());
        reservation.setStartTime(createReservationDtoIn.getTime().getStartTime());
        reservation.setEndTime(createReservationDtoIn.getTime().getEndTime());
        reservation.setCreatedTime(createReservationDtoIn.getCreatedDate());
        reservation.setStatus(status);
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

    private ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(procedure.getPrice());
        procedureDto.setDescription(procedure.getDescription());
        return procedureDto;
    }

    private List<Reservation> mergePages(Page<Reservation>... pages) {
        return Arrays.stream(pages)
                .flatMap(page -> page.getContent().stream())
                .collect(Collectors.toList());
    }

}
