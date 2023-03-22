package com.example.aranoz.web;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private BaseController baseController;

    @Test
    public void testGetHome_ReturnsExpectedView() {
        ModelAndView expectedView = new ModelAndView("Home/index");
        Mockito.when(baseController.view("Home/index")).thenReturn(expectedView);

        ModelAndView actualView = homeController.getHome();

        Assert.assertEquals(expectedView.getViewName(), actualView.getViewName());
    }

    @Test
    public void testGetHome_InvokesBaseControllerViewMethod() {
        homeController.getHome();

        Mockito.verify(baseController, Mockito.times(1)).view("Home/index");
    }
}