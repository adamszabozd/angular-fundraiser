package hu.progmasters.fundraiser.dto.exchange;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CurrentExchangeRateCommand {

    private boolean success;
    private Long timestamp;
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;

    public static CurrentExchangeRateCommand getDummyData() {
        CurrentExchangeRateCommand instance = new CurrentExchangeRateCommand();
        instance.setSuccess(true);
        instance.setTimestamp(1614594367L);
        instance.setDate(LocalDate.now().plusDays(1));
        instance.setBase("EUR");
        Map<String, Double> dummyRates = new HashMap<>();
        dummyRates.put("USD", 1.204);
        dummyRates.put("HUF", 362.617016);
        dummyRates.put("GBP", 0.862949);
        instance.setRates(dummyRates);
        return instance;
    }

    public static CurrentExchangeRateCommand getOldDummyData(LocalDate date) {
        CurrentExchangeRateCommand instance = new CurrentExchangeRateCommand();
        instance.setSuccess(true);
        instance.setTimestamp(1614594367L);
        instance.setDate(date);
        instance.setBase("EUR");
        Map<String, Double> dummyRates = new HashMap<>();
        dummyRates.put("USD", 1.333);
        dummyRates.put("HUF", 300.66);
        dummyRates.put("GBP", 0.999);
        instance.setRates(dummyRates);
        return instance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

}
