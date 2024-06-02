package org.example.dto;

import javax.validation.constraints.NotNull;

public class CreateProcedureDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;
}
