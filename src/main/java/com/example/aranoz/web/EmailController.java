package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.view.Email.ContactEmailVM;
import com.example.aranoz.domain.dtoS.model.Email.ForgottenPasswordFormDto;
import com.example.aranoz.services.email.EmailService;
import com.example.aranoz.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/Email")
public class EmailController extends BaseController {
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public EmailController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }


    //-------------DATA MANIPULATION----------------//


    @GetMapping("/contact")
    public ModelAndView getContact(){
        return super.view("Email/contact");
    }

    @PostMapping("/contact")
    public ModelAndView postContact(ContactEmailVM model) throws MessagingException {
        this.emailService.sendUserQuery(model);
        return super.view("Home/index");
    }

    @GetMapping("/forgottenPassword")
    public ModelAndView getForgottenPassword(){
        return super.view("Email/forgottenPassword");
    }

    @PostMapping("/forgottenPassword")
    public ModelAndView postForgottenPassword(@Valid @ModelAttribute(name = "forgottenPasswordFormDto") ForgottenPasswordFormDto forgottenPasswordFormDto,
                                              BindingResult bindingResult,
                                              ModelAndView modelAndView) throws MessagingException {
        if (bindingResult.hasErrors()){
            return super.view("Email/forgottenPassword",
                    modelAndView.addObject("email",forgottenPasswordFormDto.getEmail()));
        }

        this.emailService.sendForgottenPasswordLink(this.userService.findByEmail(forgottenPasswordFormDto.getEmail()));

        return super.redirect("/");
    }


    //-------------MODEL ATTRIBUTES----------------//


    @ModelAttribute(name = "forgottenPasswordFormDto")
    public ForgottenPasswordFormDto initForgottenPasswordFormDto(){
        return new ForgottenPasswordFormDto();
    }
}
