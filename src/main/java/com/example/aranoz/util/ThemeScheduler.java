package com.example.aranoz.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

@Component
public class ThemeScheduler {

    private final String LIGHT_THEME = "light";
    private final String DARK_THEME = "dark";

    @Scheduled(cron = "0 59 5,17 * * *") // Runs at 6am and 6pm
    public void changeThemeBasedOnTimeOfDay() throws IOException {
        LocalTime now = LocalTime.now();
        String theme = (now.getHour() >= 18 || now.getHour() < 6) ? DARK_THEME : LIGHT_THEME;

        if (theme.equals(DARK_THEME)) {
            CSSColorChanger.fromLightToDarkTheme();
        } else {
            CSSColorChanger.fromDarkToLightTheme();
        }
    }
}
