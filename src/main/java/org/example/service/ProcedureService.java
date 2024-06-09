package org.example.service;

import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.entity.Reservation;
import org.example.enums.ReservationStatus;
import org.example.enums.ProcedureValidity;
import org.example.repository.ProcedureRepository;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProcedureService {

    private ProcedureRepository procedureRepository;

    private ReservationRepository reservationRepository;

    public ProcedureService(ProcedureRepository procedureRepository, ReservationRepository reservationRepository) {
        this.procedureRepository = procedureRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Procedure> findAllActive() {
        return procedureRepository.findAllByStatus(ProcedureValidity.ACTIVE);
    }

    public List<Procedure> findAll() {
        return procedureRepository.findAll();
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

    public Procedure createNewProcedure(Procedure procedure) {
        if (procedureRepository.findByName(procedure.getName()) != null) {
            throw new RuntimeException("Procedure already exists!");
        }

        return procedureRepository.save(procedure);
    }

    public Procedure updateProcedure(ProcedureDto procedure) {
        Procedure existingProcedure = findById(procedure.getId());
        existingProcedure.setName(procedure.getName());
        existingProcedure.setDescription(procedure.getDescription());
        existingProcedure.setStatus(procedure.getChecked() ? ProcedureValidity.ACTIVE : ProcedureValidity.INACTIVE);
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
