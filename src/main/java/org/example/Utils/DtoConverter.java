package org.example.Utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.dto.ProcedureDto;
import org.example.entity.Procedure;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
@NoArgsConstructor
public class DtoConverter {

    public ModelMapper modelMapper;

    public ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.setName(procedure.getName());
        procedureDto.setPrice(procedure.getPrice());
        procedureDto.setDescription(procedure.getDescription());
        return procedureDto;
    }

}
