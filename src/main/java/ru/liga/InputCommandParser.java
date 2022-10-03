package ru.liga;

public class InputCommandParser {
    protected String command;
    protected String currency;
    protected String period;

    public String Parse(String inputCommand) {
        if (inputCommand == null || inputCommand == "") {
            return "Input command is null or empty";
        }

        String[] inputData = inputCommand.split(" ");

        if (inputData.length != 3) {
            return "Input command should contains 3 arguments";
        }

        if (!inputData[0].equals("rate")) {
            return "Unknown command: " + inputData[0];
        }
        command = inputData[0];

        if (!inputData[1].equals(RateForecast.EUR_DATA) &&
                !inputData[1].equals(RateForecast.USD_DATA) &&
                !inputData[1].equals(RateForecast.TRY_DATA)) {
            return "Unknown currency: " + inputData[1];
        }
        currency = inputData[1];

        if (!inputData[2].equals(RateForecast.FORECAST_TOMORROW) && !inputData[2].equals(RateForecast.FORECAST_WEEK)) {
            return "Unknown period: " + inputData[2];
        }
        period = inputData[2];

        return "";
    }
}
