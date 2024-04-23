package org.example.controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.Utils.DtoConverter;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.example.entity.UserLogin;
import org.example.service.ProcedureService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureController {

    private ProcedureService procedureService;

    private ModelMapper modelMapper;

    @PutMapping(value = "/api/procedure")
    public ResponseEntity<?> addProcedure(@RequestBody ProcedureDto procedureDto, @AuthenticationPrincipal UserLogin userLogin) {
        Procedure procedure = procedureService.createNewProcedure(procedureDto);
        if (procedure != null) {
            return ResponseEntity.ok(convertProcedureToDto(procedure));
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping(value = "/api/procedure")
    public ResponseEntity<?> updateProcedure(@RequestBody ProcedureDto procedureDto, @AuthenticationPrincipal UserLogin userLogin) {
        boolean procedureValidity = procedureService.doesProcedureExists(procedureDto.getName());
        if (!procedureValidity) {
            return ResponseEntity.badRequest().body("Error");
        }
        Procedure procedure = procedureService.updateProcedure(procedureService.findByName(procedureDto.getName()));
        return ResponseEntity.ok(convertProcedureToDto(procedure));
    }

    @DeleteMapping(value = "api/procedure")
    public ResponseEntity<?> deleteProcedure(Integer id, @AuthenticationPrincipal UserLogin userLogin) {
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

    public ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(procedure.getPrice());
        procedureDto.setDescription(procedure.getDescription());
        return procedureDto;
    }

}
