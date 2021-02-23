package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Currency;
import hu.progmasters.fundraiser.domain.ExchangeRate;
import hu.progmasters.fundraiser.dto.exchange.CurrencyOption;
import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeService(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void saveDailyRate(CurrentExchangeRateCommand rateCommand) {
        ExchangeRate exchangeRate;
        for (Map.Entry<String, Double> entry: rateCommand.getRates().entrySet()) {
            exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(Currency.valueOf(entry.getKey()));
            exchangeRate.setRate(entry.getValue());
            exchangeRate.setCurrentTime(rateCommand.getDate());
            exchangeRepository.save(exchangeRate);
        }
        exchangeRate = new ExchangeRate();
        exchangeRate.setCurrency(Currency.valueOf(rateCommand.getBase()));
        exchangeRate.setRate(1.0);
        exchangeRate.setCurrentTime(rateCommand.getDate());
        exchangeRepository.save(exchangeRate);
    }

    public double findRateByCurrency(Currency currency) {
        return exchangeRepository.findLatestRateByCurrency(currency);
    }

    public double findHistoricalRateByCurrency(Currency currency, LocalDateTime dateTime) {
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
        System.out.println("OFFSET" + offset.getTotalSeconds() / 60 / 60);
        LocalDateTime utcDateTime = dateTime.minusSeconds(offset.getTotalSeconds());
        System.out.println("UTCDATETIME" + utcDateTime);
        LocalDateTime utcNoon = utcDateTime.toLocalDate().atTime(12, 0);
        System.out.println("UTCNOON" + utcNoon);
        LocalDate dateToFindInDatabase = utcDateTime.toLocalDate();
        if (utcDateTime.isBefore(utcNoon)) {
            dateToFindInDatabase = dateToFindInDatabase.minusDays(1);
        }
        LocalDate correctedDate = offset.getTotalSeconds() > 0 ? dateToFindInDatabase.plusDays(1) : dateToFindInDatabase;
        System.out.println("CORRECTEDDATE" + correctedDate);
        return exchangeRepository.findLatestRateByCurrencyAndDate(currency, correctedDate);
    }

    public List<CurrencyOption> fetchRates() {
        List<CurrencyOption> rates = new ArrayList<>();
        CurrencyOption currencyOption;
        for (Currency currency : Currency.values()) {
            currencyOption  = new CurrencyOption();
            currencyOption.setCurrencyName(currency.name());
            currencyOption.setExchangeRate(findRateByCurrency(currency));
            rates.add(currencyOption);
        }
        return rates;
    }

}
