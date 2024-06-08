package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProcedureDto {

    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private Boolean checked;

}
