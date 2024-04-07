package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.Utils.DtoConverter;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.service.ProcedureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureController {

    public ProcedureService procedureService;

    public DtoConverter converter;

    @PostMapping("/api/procedure")
    public ResponseEntity<?> addProcedure(@RequestBody ProcedureDto procedureDto) {
        Procedure procedure = procedureService.createNewProcedure(procedureDto);
        if (procedure != null) {
            return ResponseEntity.ok(converter.convertProcedureToDto(procedure));
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/api/procedure")
    public ResponseEntity<?> updateProcedure(@RequestBody ProcedureDto procedureDto) {
        boolean procedureValidity = procedureService.doesProcedureExists(procedureDto.getName());
        if (!procedureValidity) {
            return ResponseEntity.badRequest().body("Error");
        }
        Procedure procedure = procedureService.updateProcedure(procedureService.findByName(procedureDto.getName()));
        return ResponseEntity.ok(converter.convertProcedureToDto(procedure));
    }

    @DeleteMapping("api/procedure")
    public ResponseEntity<?> deleteProcedure(Integer id) {
        try {
            if (!procedureService.doesProcedureExists(id)) {
                return ResponseEntity.badRequest().body("Delete procedure fail. Procedure doesn't exists.");
            }
            Procedure deletedProcedure = procedureService.deleteProcedure(id);
            return ResponseEntity.ok("Procedure deleted successfully.");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Delete procedure fail. There may be reservations for this procedure.");
        }

    }

}
