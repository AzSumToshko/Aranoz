package com.example.aranoz.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class CSSColorChangerTest {

    private static final String CSS_DIRECTORY = "src/main/resources/static/css";
    private static final String TEST_DIRECTORY = "src/test/resources/css";

    private CSSColorChanger cssColorChanger;

    @BeforeEach
    public void setUp() {
        cssColorChanger = new CSSColorChanger();
    }

    @Test
    public void fromLightToDarkTheme_should_change_light_to_dark_theme() throws IOException {
        String testFileContent = "body {\n" +
                "    background-color: #fff;\n" +
                "}";
        String expectedFileContent = "body {\n" +
                "    background-color: #362f2f;\n" +
                "}";
        File testFile = createTestFile(testFileContent);

        cssColorChanger.fromLightToDarkTheme();

        assertFileContent(testFile, expectedFileContent);
    }

    @Test
    public void fromDarkToLightTheme_should_change_dark_to_light_theme() throws IOException {
        String testFileContent = "body {\n" +
                "    background-color: #362f2f;\n" +
                "}";
        String expectedFileContent = "body {\n" +
                "    background-color: #fff;\n" +
                "}";
        File testFile = createTestFile(testFileContent);

        cssColorChanger.fromDarkToLightTheme();

        assertFileContent(testFile, expectedFileContent);
    }

    private File createTestFile(String fileContent) throws IOException {
        File testDirectory = new File(TEST_DIRECTORY);
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        File testFile = new File(TEST_DIRECTORY + "/test.css");
        testFile.createNewFile();
        Files.write(testFile.toPath(), Arrays.asList(fileContent));
        return testFile;
    }

    private void assertFileContent(File file, String expectedFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        String actualFileContent = sb.toString();
        assertEquals(expectedFileContent, actualFileContent);
    }
}