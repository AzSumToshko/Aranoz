package com.example.aranoz.services.email;

import com.example.aranoz.domain.dtoS.view.Email.ContactEmailVM;
import com.example.aranoz.domain.entities.User;

import javax.mail.MessagingException;

public interface EmailService {
    void sendUserQuery(ContactEmailVM model) throws MessagingException;
    void sendForgottenPasswordLink(User byEmail) throws MessagingException;
    void sendAcceptedOrderEmail(String to,Long productsCount, Double total,String orderID) throws MessagingException;
}
