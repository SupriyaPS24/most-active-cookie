package com.app.service;


import java.io.File;
import java.io.IOException;
import java.util.*;
import com.app.exception.CookieNotFoundException;

public class CookieAnalyzer {

    private final CookieProcessor processor;

    public CookieAnalyzer(CookieProcessor processor) {
        this.processor = processor;
    }

    public List<String> analyze(File logFile, String date) throws IOException {
        List<String> mostActiveCookies = processor.findMostActiveCookies(logFile, date);

        if (mostActiveCookies.isEmpty()) {
            throw new CookieNotFoundException("No cookies found for the given date.");
        }

        return mostActiveCookies;

    }
}