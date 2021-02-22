package hu.progmasters.fundraiser.dto.fund;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DailyDonation {
    private final String date;
    private final double amount;

    public DailyDonation(LocalDateTime date, double amount) {
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.amount = amount;
    }

    public DailyDonation(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
