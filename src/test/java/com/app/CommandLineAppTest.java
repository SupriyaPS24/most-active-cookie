package com.app;

import com.app.cli.CommandLineApp;
import com.app.exception.CookieNotFoundException;
import com.app.exception.InvalidFileException;
import com.app.service.CookieAnalyzer;
import com.app.service.CookieProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineAppTest {

    private CookieAnalyzer analyzer;

    @BeforeEach
    public void setUp() {
        analyzer = new CookieAnalyzer(new CookieProcessor());
    }

    @Test
    public void testMissingArguments() throws IOException {
        File testFile = File.createTempFile("test-log", ".csv");
        String[] args = {"-f", testFile.getAbsolutePath()};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandLineApp.main(args));
        assertEquals("Invalid arguments. Usage: java CommandLineApp -f <logfile.csv> -d <date>", exception.getMessage());

        testFile.deleteOnExit();
    }

    @Test
    public void testExtraArguments() throws IOException {
        File testFile = File.createTempFile("test-log", ".csv");
        String[] args = {"-f", testFile.getAbsolutePath(), "-d", "2025-03-29", "--extra", "value"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandLineApp.main(args));
        assertEquals("Invalid arguments. Usage: java CommandLineApp -f <logfile.csv> -d <date>", exception.getMessage());

        testFile.deleteOnExit();
    }

    @Test
    public void testInvalidArgumentOrder() throws IOException {
        File testFile = File.createTempFile("test-log", ".csv");
        String[] args = {"-d", "2025-03-29", "-f", testFile.getAbsolutePath()};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandLineApp.main(args));
        assertEquals("Invalid arguments. Usage: java CommandLineApp -f <logfile.csv> -d <date>", exception.getMessage());

        testFile.deleteOnExit();
    }

    @Test
    public void testFileDoesNotExist() {
        File file = new File("nonexistent.csv");
        if (file.exists()) {
            file.delete();
        }
        String[] args = {"-f", "nonexistent.csv", "-d", "2025-03-29"};
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> CommandLineApp.main(args));
        assertEquals("The specified file does not exist: nonexistent.csv", exception.getMessage());
    }

    @Test
    public void testInvalidFileFormat() throws IOException {
        File file = new File("invalid-file.txt");
        if (!file.exists()) {
            file.createNewFile(); // Create a non-CSV file
        }
        String[] args = {"-f", file.getAbsolutePath(), "-d", "2025-03-29"};
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> CommandLineApp.main(args));
        assertEquals("Error: Invalid file format. Expected a .csv file.", exception.getMessage());

        file.deleteOnExit();
    }

    @Test
    public void testInvalidDateFormat() throws IOException {
        File testFile = File.createTempFile("test-log", ".csv");
        String[] args = {"-f", testFile.getAbsolutePath(), "-d", "03-29-2025"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommandLineApp.main(args));
        assertEquals("Error: Invalid date format. Expected format: yyyy-MM-dd (e.g., 2018-12-09)", exception.getMessage());
        testFile.deleteOnExit();
    }

    @Test
    public void testValidArgumentsWithNoCookiesFound() throws IOException {
        File testFile = File.createTempFile("test-log", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("cookie,timestamp\n"); // CSV Header
            writer.write("cookie1,2025-03-29T10:00:00+00:00\n");
        }
        String[] args = {"-f", testFile.getAbsolutePath(), "-d", "2025-03-28"};
        CookieNotFoundException exception = assertThrows(CookieNotFoundException.class, () -> CommandLineApp.main(args));
        assertEquals("No cookies found for the given date.", exception.getMessage());
        testFile.deleteOnExit();
    }

    @Test
    public void testValidArgumentsWithCookiesFound() throws IOException {
        File testFile = File.createTempFile("valid-log", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("cookie,timestamp\n"); // CSV Header
            writer.write("cookie1,2025-03-29T10:00:00+00:00\n");
            writer.write("cookie2,2025-03-29T12:30:00+00:00\n");
            writer.write("cookie1,2025-03-29T23:59:59+00:00\n");
        }
        String[] args = {"-f", testFile.getAbsolutePath(), "-d", "2025-03-29"};
        assertDoesNotThrow(() -> CommandLineApp.main(args));
        testFile.deleteOnExit();
    }
}
