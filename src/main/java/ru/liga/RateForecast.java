package ru.liga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class RateForecast {

    static final String EUR_DATA = "EUR";
    static final String USD_DATA = "USD";
    static final String TRY_DATA = "TRY";

    static final String FORECAST_TOMORROW = "tomorrow";
    static final String FORECAST_WEEK = "week";

    static final String DELIMITER = ";";
    static final Integer DEPTH = 7;

    private final List<Rate> allRates = new ArrayList<>();
    private final List<Rate> forecastRates = new ArrayList<>();

    void loadAllArchiveData() {
        loadArchiveData(EUR_DATA);
        loadArchiveData(USD_DATA);
        loadArchiveData(TRY_DATA);
    }

    Integer getAllDataCount() {
        return allRates.size();
    }

    List<Rate> getForecast(String currency, String period) {
        forecastRates.clear();

        Integer periodLimit = period.equals(FORECAST_TOMORROW) ? 1 : 7;
        Integer dayIndex = 1;

        while (forecastRates.size() < periodLimit) {
            List<Rate> rates = getRates(currency);

            Double sum = rates.stream().mapToDouble(i -> i.rate).sum();
            Double mean = sum / DEPTH;

            LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
            ldt = ldt.plusDays(dayIndex++);
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            Rate forecastRate = new Rate(date, mean);
            forecastRates.add(forecastRate);
        }

        return forecastRates;
    }

    private List<Rate> getRates(String currency) {
        List<Rate> rates = new ArrayList<>();
        rates.addAll(forecastRates);

        Integer limit = DEPTH - rates.size();

        if (limit > 0) {
            List<Rate> archiveRates = allRates.stream().filter(r -> r.currency.equals(currency))
                    .sorted(Comparator.comparing(s -> s.date))
                    .sorted(Collections.reverseOrder())
                    .limit(limit)
                    .collect(Collectors.toList());

            rates.addAll(archiveRates);
        }

        return rates;
    }

    private void loadArchiveData(String currency) {
        InputStream fileStream = getFileFromResourceAsStream(currency + ".csv");

        try (BufferedReader csvReader = new BufferedReader(new InputStreamReader(fileStream))) {

            String line = csvReader.readLine();
            Boolean isFirstLineSkipped = false;

            while (line != null) {
                String[] data = line.split(DELIMITER);

                if (isFirstLineSkipped) {
                    Rate rate = new Rate(data, currency);
                    allRates.add(rate);
                } else {
                    isFirstLineSkipped = true;
                }

                line = csvReader.readLine();
            }
        } catch (IOException | ParseException ioe) {
            ioe.printStackTrace();
        }
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
