package com.example.aranoz.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ThemeSchedulerTest {

    @MockBean
    private CSSColorChanger cssColorChanger;

    private ThemeScheduler themeScheduler;

    @BeforeEach
    public void setUp() {
        themeScheduler = new ThemeScheduler();
    }

    @Test
    public void testChangeThemeBasedOnTimeOfDay_whenLightTheme_shouldCallFromDarkToLightTheme() throws IOException {
        Mockito.doNothing().when(cssColorChanger).fromDarkToLightTheme();

        themeScheduler.changeThemeBasedOnTimeOfDay();

        verify(cssColorChanger, times(1)).fromDarkToLightTheme();
        verify(cssColorChanger, times(0)).fromLightToDarkTheme();
    }

    @Test
    public void testChangeThemeBasedOnTimeOfDay_whenDarkTheme_shouldCallFromLightToDarkTheme() throws IOException {
        Mockito.doNothing().when(cssColorChanger).fromLightToDarkTheme();

        themeScheduler.changeThemeBasedOnTimeOfDay();

        verify(cssColorChanger, times(0)).fromDarkToLightTheme();
        verify(cssColorChanger, times(1)).fromLightToDarkTheme();
    }
}