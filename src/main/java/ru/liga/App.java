package ru.liga;

import java.util.List;
import java.util.Scanner;

/**
 * Exchange rate forecast console application
 *
 */
public class App {

    public static void main(String[] args) {

        RateForecast rateForecast = new RateForecast();

        System.out.println("Loading data...");
        rateForecast.loadAllArchiveData();
        System.out.println("Load completed. Loaded " + rateForecast.getAllDataCount() + " rows");


        while(true) {
            System.out.println("Waiting for command");

            Scanner scanner = new Scanner(System.in);
            String commandText = scanner.nextLine();

            if (commandText.equals("exit")) {
                break;
            }

            InputCommandParser commandParser = new InputCommandParser();
            String parseResult = commandParser.Parse(commandText);

            if (!parseResult.equals("")) {
                System.out.println(parseResult);
            } else {
                List<Rate> rates = rateForecast.getForecast(commandParser.currency, commandParser.period);

                for(Rate rate : rates)
                    System.out.println(rate.GetValue());
            }
        }
    }
}
