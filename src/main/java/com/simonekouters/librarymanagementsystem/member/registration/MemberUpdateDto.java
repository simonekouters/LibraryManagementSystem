package com.simonekouters.librarymanagementsystem.member.registration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberUpdateDto {
    @Size(min = 1, message = "first name should not be blank")
    private String firstName;

    @Size(min = 1, message = "last name should not be blank")
    private String lastName;

    @ValidPassword(message = "Password must have at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @Email(message = "not a valid email address")
    private String email;
}
