package com.app;

import com.app.exception.CookieNotFoundException;
import com.app.exception.InvalidFileException;
import com.app.service.CookieAnalyzer;
import com.app.service.CookieProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CookieAnalyzerTest {

    private CookieAnalyzer analyzer;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        analyzer = new CookieAnalyzer(new CookieProcessor());

        // Create a temporary test log file
        testFile = File.createTempFile("test-log", ".csv");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("cookie,timestamp\n");
            writer.write("cookie1,2025-03-29T12:00:00Z\n");
            writer.write("cookie2,2025-03-29T13:00:00Z\n");
            writer.write("cookie2,2025-03-29T14:00:00Z\n");
        }
    }

    @Test
    public void testAnalyze_oneCookieForGivenDate() throws Exception {
        List<String> result = analyzer.analyze(testFile, "2025-03-29");
        System.out.println("result=="+result);
        assertEquals(1, result.size());
        assertEquals("cookie2", result.get(0));  // Most active cookie
    }

    @Test
    public void testAnalyze_multipleCookiesForGivenDate() throws Exception {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("cookie1,2025-03-29T15:00:00Z\n");
        }

        List<String> result = analyzer.analyze(testFile, "2025-03-29");
        assertTrue(result.contains("cookie1"));
        assertTrue(result.contains("cookie2"));
    }

    @Test
    public void testAnalyze_noCookiesForGivenDate() {
        Exception exception = assertThrows(CookieNotFoundException.class, () -> {
            analyzer.analyze(testFile, "2025-03-30");
        });
        assertEquals("No cookies found for the given date.", exception.getMessage());
    }

    @Test
    public void testAnalyze_fileNotFound() {
        File nonExistentFile = new File("nonexistent.csv");
        assertThrows(IOException.class, () -> analyzer.analyze(nonExistentFile, "2025-03-29"));
    }

    @Test
    public void testAnalyze_invalidDateFormat() {
        Exception exception = assertThrows(CookieNotFoundException.class, () -> {
            analyzer.analyze(testFile, "invalid-date");
        });
        assertEquals("No cookies found for the given date.", exception.getMessage());
    }

    @Test
    public void testAnalyze_emptyFile() throws Exception {
        File emptyFile = File.createTempFile("empty-log", ".csv");
        Files.write(emptyFile.toPath(), new byte[0]);
        Exception exception = assertThrows(InvalidFileException.class, () -> {
            analyzer.analyze(emptyFile, "2025-03-29");
        });

        assertEquals("Invalid CSV format: The file is empty.", exception.getMessage());
    }

    @Test
    public void testAnalyze_invalidCSVFormat() throws Exception {
        File invalidFile = File.createTempFile("invalid-log", ".csv");
        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("invaliddata\n");
        }

        Exception exception = assertThrows(InvalidFileException.class, () -> analyzer.analyze(invalidFile, "2025-03-29"));
        System.out.println(exception);
        assertTrue(exception.getMessage().contains("Invalid CSV format: No valid cookie data found."));
    }

}
