package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.ExchangeRate;
import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeService(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void saveDailyRate(CurrentExchangeRateCommand rateCommand) {
        ExchangeRate exchangeRate = new ExchangeRate(rateCommand);
        exchangeRepository.save(exchangeRate);
    }

}
