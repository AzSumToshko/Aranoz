package com.example.aranoz.domain.dtoS.model.User;

import com.example.aranoz.domain.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserFormDto {
    @NotNull
    @Size(min = 3,max = 20)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 20)
    private String lastName;

    @Email
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    private String password;

    private Role role;
}
