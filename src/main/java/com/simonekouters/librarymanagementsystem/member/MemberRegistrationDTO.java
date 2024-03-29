package com.simonekouters.librarymanagementsystem.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class MemberRegistrationDTO {
    @Valid

    @NotNull(message = "first name is mandatory")
    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @NotNull(message = "last name is mandatory")
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @ValidPassword
    private String password;

    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "not a valid email address")
    private String email;
}
