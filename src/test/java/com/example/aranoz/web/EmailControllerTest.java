package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Email.ForgottenPasswordFormDto;
import com.example.aranoz.domain.dtoS.view.Email.ContactEmailVM;
import com.example.aranoz.services.email.EmailService;
import com.example.aranoz.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    public void testGetContact() throws Exception {
        mockMvc.perform(get("/Email/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("Email/contact"));
    }

    @Test
    public void testPostContact() throws Exception {
        ContactEmailVM contactEmailVM = new ContactEmailVM();
        mockMvc.perform(post("/Email/contact").flashAttr("model", contactEmailVM))
                .andExpect(status().isOk())
                .andExpect(view().name("Home/index"));
        verify(emailService, times(1)).sendUserQuery(contactEmailVM);
    }

    @Test
    public void testGetForgottenPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/forgottenPassword"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("Email/forgottenPassword"));
    }

    @Test
    public void testPostForgottenPasswordWithValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/forgottenPassword")
                        .param("email", "example@test.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    public void testPostForgottenPasswordWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/forgottenPassword")
                        .param("email", "invalid_email"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("Email/forgottenPassword"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("email"));
    }

    @Test
    public void testInitForgottenPasswordFormDto() {
        EmailController controller = new EmailController(emailService,userService);
        ForgottenPasswordFormDto formDto = controller.initForgottenPasswordFormDto();
        // Assert that the formDto is not null
        assertNotNull(formDto);
    }
}