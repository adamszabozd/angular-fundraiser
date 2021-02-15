package hu.progmasters.fundraiser.domain;

import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable = false, updatable = false)
    private Long id;

    @Column(name = "date", unique = true, nullable = false, updatable = false)
    private LocalDate currentTime;

    @Column(name = "base_currency", nullable = false, updatable = false)
    private String baseCurrency;

    @Column(name = "USD", nullable = false, updatable = false)
    private Double USD;

    @Column(name = "HUF", nullable = false, updatable = false)
    private Double HUF;

    @Column(name = "GBP", nullable = false, updatable = false)
    private Double GBP;

    public ExchangeRate() {
    }

    public ExchangeRate(CurrentExchangeRateCommand exchangeRateCommand) {
        this.currentTime = exchangeRateCommand.getDate().plusDays(1);
        this.baseCurrency = exchangeRateCommand.getBase();
        this.USD = exchangeRateCommand.getRates().get("USD");
        this.HUF = exchangeRateCommand.getRates().get("HUF");
        this.GBP = exchangeRateCommand.getRates().get("GBP");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDate currentTime) {
        this.currentTime = currentTime;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Double getUSD() {
        return USD;
    }

    public void setUSD(Double USD) {
        this.USD = USD;
    }

    public Double getHUF() {
        return HUF;
    }

    public void setHUF(Double HUF) {
        this.HUF = HUF;
    }

    public Double getGBP() {
        return GBP;
    }

    public void setGDP(Double GBP) {
        this.GBP = GBP;
    }

}
