package com.example.aranoz.util;

import java.io.*;

public class CSSColorChanger {

    private static final String CSS_DIRECTORY = "src/main/resources/static/css";
    private static final String LIGHT_THEME = "#fff";
    private static final String DARK_THEME = "#362f2f";

    public static void fromLightToDarkTheme() throws IOException {
        File directory = new File(CSS_DIRECTORY);
        File[] cssFiles = directory.listFiles((dir, name) -> name.endsWith(".css"));
        for (File file : cssFiles) {
            String fileContent = readFile(file);
            String newFileContent = fileContent.replaceAll(LIGHT_THEME, DARK_THEME);
            writeFile(file, newFileContent);
        }
    }

    public static void fromDarkToLightTheme() throws IOException {
        File directory = new File(CSS_DIRECTORY);
        File[] cssFiles = directory.listFiles((dir, name) -> name.endsWith(".css"));
        for (File file : cssFiles) {
            String fileContent = readFile(file);
            String newFileContent = fileContent.replaceAll(DARK_THEME, LIGHT_THEME);
            writeFile(file, newFileContent);
        }
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        return sb.toString();
    }

    private static void writeFile(File file, String fileContent) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
    }
}
