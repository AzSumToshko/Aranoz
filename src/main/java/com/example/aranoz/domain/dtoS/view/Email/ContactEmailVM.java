package com.example.aranoz.domain.dtoS.view.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactEmailVM {
    private String message;

    private String subject;

    private String fullName;

    private String email;
}
