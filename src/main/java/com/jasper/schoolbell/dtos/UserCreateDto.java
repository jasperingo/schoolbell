package com.jasper.schoolbell.dtos;

import com.jasper.schoolbell.validators.UserPhoneNumberIsUnique;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserCreateDto {
    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Size(min = 14, max = 14)
    @Pattern(regexp = "^\\+234(\\d)+")
    @UserPhoneNumberIsUnique
    private String phoneNumber;

    @NotNull
    @Size(min = 6)
    private String password;
}
