package com.jasper.schoolbell.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventCreateDto {
    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;
}
