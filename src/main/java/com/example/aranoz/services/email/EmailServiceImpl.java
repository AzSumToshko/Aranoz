package com.example.aranoz.services.email;

import com.example.aranoz.domain.dtoS.view.Email.ContactEmailVM;
import com.example.aranoz.domain.entities.User;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import static com.example.aranoz.constants.Constants.*;

@Service
public class EmailServiceImpl implements EmailService{
    private Properties properties;
    private Session session;

    public EmailServiceImpl() {
        this.properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.office365.com");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_ADDRESS_PASSWORD);
            }
        });
    }

    @Override
    public void sendUserQuery(ContactEmailVM model) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_ADDRESS));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(EMAIL_ADDRESS));
        message.setSubject(String.format(EMAIL_SUBJECT_FORMAT,model.getSubject(),model.getEmail()));

        String msg = String.format(EMAIL_MESSAGE_FORMAT,model.getFullName(),model.getMessage());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, EMAIL_CONTENT_TYPE);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    @Override
    public void sendForgottenPasswordLink(User model) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_ADDRESS));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(model.getEmail()));
        message.setSubject(FORGOTTEN_EMAIL_SUBJECT);

        String msg = String.format(FORGOTTEN_EMAIL_MESSAGE_FORMAT,CHANGE_PASSWORD_LINK + model.getId());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, EMAIL_CONTENT_TYPE);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    @Override
    public void sendAcceptedOrderEmail(String to,Long productsCount, Double total,String orderID) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_ADDRESS));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(EMAIL_ACCEPTED_ORDER);

        String msg = String.format(EMAIL_ACCEPTED_ORDER_FORMAT,productsCount,total,String.format(ORDER_INFO_FORMAT, orderID));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, EMAIL_CONTENT_TYPE);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
