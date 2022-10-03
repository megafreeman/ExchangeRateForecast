package ru.liga;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rate implements Comparable<Rate> {

    protected Integer nominal;
    protected Date date;
    protected Double rate;
    protected String currency;

    public Rate(Date date, Double rate) {
        this.date = date;
        this.rate = rate;
    }

    public Rate(String[] input, String currency) throws ParseException {
        //this.nominal = Integer.parseInt(input[0]);
        this.date = new SimpleDateFormat("MM/dd/yyyy").parse(input[1]);
        this.rate = Double.parseDouble(input[2]);
        this.currency = currency;
    }

    public String GetValue(){
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        String forecastValue = decimalFormat.format(rate);

        DateFormat df = new SimpleDateFormat("E, dd.MM.yyyy");
        String dateValue = df.format(date);

        return dateValue + " - " + forecastValue;
    }

    @Override
    public int compareTo(Rate obj) {
        if (date.before(obj.date)) {
            return -1;
        } else if (date.after(obj.date)) {
            return 1;
        } else {
            return 0;
        }
    }
}
