package com.app.cli;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.app.exception.CookieNotFoundException;
import com.app.exception.InvalidFileException;
import com.app.service.CookieAnalyzer;
import com.app.service.CookieProcessor;


public class CommandLineApp {

    private static final CookieAnalyzer analyzer = new CookieAnalyzer(new CookieProcessor());
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        try {
            validateArguments(args);

            String logFilePath = args[1];
            String date = args[3];

            validateFile(logFilePath);
            validateDateFormat(date);

            File logFile = new File(logFilePath);
            List<String> result = analyzer.analyze(logFile, date);

            if (result.isEmpty()) {
                throw new CookieNotFoundException("No cookies found for the given date.");
            }

            result.forEach(System.out::println);
        } catch (IllegalArgumentException | InvalidFileException | CookieNotFoundException e) {
            System.err.println(e.getMessage());
            throw e; //test
        } catch (IOException e) {
            System.err.println("Error processing the log file: " + e.getMessage());
        }
    }

    private static void validateArguments(String[] args) {
        if (args.length != 4 || !"-f".equals(args[0]) || !"-d".equals(args[2])) {
            throw new IllegalArgumentException("Invalid arguments. Usage: java CommandLineApp -f <logfile.csv> -d <date>");
        }
    }

    private static void validateFile(String logFilePath) {
        if (!logFilePath.endsWith(".csv")) {
            throw new InvalidFileException("Error: Invalid file format. Expected a .csv file.");
        }
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            throw new InvalidFileException("The specified file does not exist: " + logFilePath);
        }
    }

    private static void validateDateFormat(String date) {
        try {
            DATE_FORMAT.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error: Invalid date format. Expected format: yyyy-MM-dd (e.g., 2018-12-09)");
        }
    }

}
