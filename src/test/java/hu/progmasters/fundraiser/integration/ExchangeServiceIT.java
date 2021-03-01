package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.domain.Currency;
import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.repository.ExchangeRepository;
import hu.progmasters.fundraiser.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
class ExchangeServiceIT {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeRepository exchangeRepository;

    void init() {
        CurrentExchangeRateCommand command = CurrentExchangeRateCommand.getDummyData();
        exchangeService.saveDailyRate(command);
        CurrentExchangeRateCommand command2 = CurrentExchangeRateCommand.getOldDummyData(LocalDate.parse("2020-10-10"));
        exchangeService.saveDailyRate(command2);
    }

    @Test
    void testSaveDailyRate() {
        init();
        assertEquals(8, exchangeRepository.count());
    }

    @Test
    void testFindRateByCurrency() {
        init();
        assertEquals(1, exchangeService.findRateByCurrency(Currency.EUR));
        assertEquals(1.204, exchangeService.findRateByCurrency(Currency.USD));
        assertEquals(362.617016, exchangeService.findRateByCurrency(Currency.HUF));
        assertEquals(0.862949, exchangeService.findRateByCurrency(Currency.GBP));
    }

    @Test
    void testFindHistoricalRateByCurrency() {
        init();
        assertEquals(1, exchangeService.findHistoricalRateByCurrency(Currency.EUR, LocalDateTime.parse("2020-10-10T12:00:00")));
        assertEquals(1.333, exchangeService.findHistoricalRateByCurrency(Currency.USD, LocalDateTime.parse("2020-10-10T12:00:00")));
        assertEquals(300.66, exchangeService.findHistoricalRateByCurrency(Currency.HUF, LocalDateTime.parse("2020-10-10T12:00:00")));
        assertEquals(0.999, exchangeService.findHistoricalRateByCurrency(Currency.GBP, LocalDateTime.parse("2020-10-10T12:00:00")));
    }

}
