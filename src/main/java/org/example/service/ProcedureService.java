package org.example.service;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ProcedureCreationDto;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.RESERVATION_STATUS;
import org.example.enums.RESERVATION_VALIDITY;
import org.example.repository.ProcedureRepository;
import org.example.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureService {

    @Autowired
    public ProcedureRepository procedureRepository;

    @Autowired
    public ReservationRepository reservationRepository;

    public List<Procedure> findAll() {
        return procedureRepository.findAll();
    }

    public List<Procedure> findAllActive() {
        return procedureRepository.findAllByValidity(RESERVATION_VALIDITY.VALID);
    }

    public Procedure findByName(String name) {
        return procedureRepository.findByName(name);
    }
    public boolean doesProcedureExists(Integer id) {
        Optional<Procedure> procedure = procedureRepository.findById(id);
        return procedure.isPresent();
    }

    public boolean doesProcedureExists(String name) {
        return findByName(name) != null;
    }

    public Procedure createNewProcedure(ProcedureDto newProcedure) {
        if (procedureRepository.findByName(newProcedure.getName()) != null) {
            throw new RuntimeException("Procedure already exists!");
        }
        Procedure procedure = new Procedure(newProcedure.getName(), newProcedure.getDescription(), newProcedure.getPrice());
        return procedureRepository.save(procedure);
    }

    public Procedure updateProcedure(Procedure procedure) {
        Optional<Procedure> existingProcedure = procedureRepository.findById(procedure.getId());
        if (existingProcedure.isPresent()) {
            existingProcedure.get().setName(procedure.getName());
            existingProcedure.get().setDescription(procedure.getDescription());
            existingProcedure.get().setStatus(procedure.getStatus());
            existingProcedure.get().setPrice(procedure.getPrice());
            return procedureRepository.save(existingProcedure.get());
        } else {
            throw new RuntimeException("Procedure doesn't exist!");
        }
    }

    public Procedure deleteProcedure(Integer id) {
        Optional<Procedure> procedure = procedureRepository.findById(id);
        if (procedure.isPresent()) {
            List<Reservation> reservationList = reservationRepository.findReservationsByProcedureId(procedure.get().getId());
            if (!reservationList.isEmpty()) {
                for (Reservation reservation : reservationList) {
                    reservation.setStatus(RESERVATION_STATUS.CANCELED);
                    reservationRepository.save(reservation);
                }
            }
            procedureRepository.deleteById(id);
            return procedure.get();
        }
        throw new RuntimeException("Procedure doens't exist!");
    }
}
