package com.app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.app.exception.InvalidFileException;

public class CookieProcessor {

    public List<String> findMostActiveCookies(File logFile, String date) throws IOException {
        Map<String, Integer> cookieCountMap = new HashMap<>();
        boolean validDataFound = false;

        try (BufferedReader reader = Files.newBufferedReader(logFile.toPath())) {
            String header = reader.readLine(); // Read header (Assuming the first line is the header)

            if (header == null) {
                throw new InvalidFileException("Invalid CSV format: The file is empty.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length != 2) {
                    throw new InvalidFileException("Invalid CSV format: Each line must contain exactly 2 values (cookie, timestamp).");
                }

                String cookie = parts[0].trim();
                String timestampStr = parts[1].trim();

                try {
                    ZonedDateTime timestamp = ZonedDateTime.parse(timestampStr);
                    validDataFound = true;
                    if (timestamp.toLocalDate().toString().equals(date)) {
                        cookieCountMap.put(cookie, cookieCountMap.getOrDefault(cookie, 0) + 1);
                    }
                } catch (Exception e) {
                    throw new InvalidFileException("Invalid CSV format: Unable to parse timestamp '" + timestampStr + "'. Expected format: YYYY-MM-DDTHH:MM:SS+TZ");
                }
            }
        }

        if (!validDataFound) {
            throw new InvalidFileException("Invalid CSV format: No valid cookie data found.");
        }

        int maxCount = cookieCountMap.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        return cookieCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
