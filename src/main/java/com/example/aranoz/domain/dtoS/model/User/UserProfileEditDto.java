package com.example.aranoz.domain.dtoS.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileEditDto {

    private String id;

    @NotBlank
    @Size(min = 3,max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3,max = 20)
    private String lastName;

    @Email
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$")
    private String email;
}
