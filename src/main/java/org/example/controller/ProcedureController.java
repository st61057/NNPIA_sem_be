package org.example.controller;

import org.example.dto.CreateProcedureDto;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.enums.ProcedureValidity;
import org.example.service.ProcedureService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
public class ProcedureController {

    private final ProcedureService procedureService;

    private final ModelMapper modelMapper;

    public ProcedureController(ProcedureService procedureService, ModelMapper modelMapper) {
        this.procedureService = procedureService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/public/procedures-active")
    public ResponseEntity<?> getAllActive() {
        List<Procedure> procedures = procedureService.findAllActive();
        return ResponseEntity.ok(procedures.stream().map(this::convertProcedureToDto).collect(Collectors.toList()));
    }

    @GetMapping("/public/procedures")
    public ResponseEntity<?> getAll() {
        List<Procedure> procedures = procedureService.findAll();
        return ResponseEntity.ok(procedures.stream().map(this::convertProcedureToDto).collect(Collectors.toList()));
    }

    @PostMapping(value = "/api/procedure-add")
    public ResponseEntity<?> addProcedure(@RequestBody CreateProcedureDto procedureDto) {
        Procedure procedure = convertToEntity(procedureDto);
        Procedure createdProcedure = procedureService.createNewProcedure(procedure);
        if (createdProcedure != null) {
            return ResponseEntity.ok(convertProcedureToDto(createdProcedure));
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @PutMapping(value = "/api/procedure-update")
    public ResponseEntity<?> updateProcedure(@RequestBody ProcedureDto procedureDto) {
        boolean procedureValidity = procedureService.doesProcedureExists(procedureDto.getName());
        if (!procedureValidity) {
            return ResponseEntity.badRequest().body("Error");
        }
        Procedure procedure = procedureService.updateProcedure(procedureDto);
        return ResponseEntity.ok(convertProcedureToDto(procedure));
    }

    @DeleteMapping(value = "/api/procedure-delete/{id}")
    public ResponseEntity<?> deleteProcedure(@PathVariable Integer id) {
        try {
            if (!procedureService.doesProcedureExists(id)) {
                return ResponseEntity.badRequest().body("Delete procedure fail. Procedure doesn't exists.");
            }
            Procedure deletedProcedure = procedureService.deleteProcedure(id);
            return ResponseEntity.ok(deletedProcedure);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Delete procedure fail. There may be reservations for this procedure.");
        }
    }

    private ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(procedure.getPrice());
        procedureDto.setDescription(procedure.getDescription());
        procedureDto.setChecked(procedure.getStatus().equals(ProcedureValidity.ACTIVE));
        return procedureDto;
    }

    private Procedure convertToEntity(CreateProcedureDto createProcedureDto) {
        Procedure procedure = modelMapper.map(createProcedureDto, Procedure.class);
        procedure.setName(createProcedureDto.getName());
        procedure.setPrice(createProcedureDto.getPrice());
        procedure.setDescription(createProcedureDto.getDescription());
        procedure.setStatus(createProcedureDto.getStatus() ? ProcedureValidity.ACTIVE : ProcedureValidity.INACTIVE);
        return procedure;
    }

}
