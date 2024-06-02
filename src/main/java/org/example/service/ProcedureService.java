package org.example.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.enums.ReservationValidity;
import org.example.repository.ProcedureRepository;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureService {

    private ProcedureRepository procedureRepository;

    private ReservationRepository reservationRepository;

    public List<Procedure> findAll() {
        return procedureRepository.findAll();
    }

    public List<Procedure> findAllActive() {
        return procedureRepository.findAllByStatus(ReservationValidity.VALID);
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
        Procedure existingProcedure = findById(procedure.getId());
        existingProcedure.setName(procedure.getName());
        existingProcedure.setDescription(procedure.getDescription());
        existingProcedure.setStatus(procedure.getStatus());
        existingProcedure.setPrice(procedure.getPrice());
        return procedureRepository.save(existingProcedure);
    }

    public Procedure deleteProcedure(Integer id) {
        Procedure procedure = findById(id);
        List<Reservation> reservationList = reservationRepository.findReservationsByProcedureId(procedure.getId());
        if (!reservationList.isEmpty()) {
            for (Reservation reservation : reservationList) {
                reservation.setStatus(ReservationStatus.CANCELED);
                reservationRepository.save(reservation);
            }
        }
        procedureRepository.deleteById(id);
        return procedure;
    }

    public Procedure findById(Integer id) {
        Optional<Procedure> procedure = procedureRepository.findById(id);
        if (procedure.isPresent()) {
            return procedure.get();
        } else {
            throw new NoSuchElementException("Procedure with id: " + id + " was not found!");
        }
    }
}
