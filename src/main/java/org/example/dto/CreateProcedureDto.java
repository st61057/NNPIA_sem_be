package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateProcedureDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private Boolean status;
}
