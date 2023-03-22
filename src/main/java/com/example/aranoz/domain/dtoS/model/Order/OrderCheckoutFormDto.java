package com.example.aranoz.domain.dtoS.model.Order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCheckoutFormDto {

    //form information
    @NotNull
    @Size(min = 3,max = 20)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 20)
    private String lastName;

    private String company;

    @NotNull
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}(|-| )\\d{3}(|-| )\\d{3}")
    private String phoneNumber;

    @NotNull
    @Email
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$")
    private String email;

    @NotNull
    @Size(min = 5,max = 50)
    private String addressOne;

    private String addressTwo;

    @NotNull
    @Size(min = 3,max = 20)
    private String city;

    @NotNull
    @Size(min = 4,max = 8)
    private String zip;

    private String message;

    //data information
    private Double total;

    private String stringCookies;
}
