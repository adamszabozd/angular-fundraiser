package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.service.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class ExchangeController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);
    //TODO - Review: Ezeket is szebb kirakni az application.yaml-be, legalább a változó értékeket belőle
    private static final String EXCHANGE_API_URL = "http://data.fixer.io/api/latest";
    private static final String ACCESS_KEY = "?access_key=ccbcdaedbc16e8cbc37bd02fb523823a";
    private static final String BASE_CURRENCY = "&base=EUR";
    private static final String TARGET_CURRENCY = "&symbols=USD,HUF,GBP";

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Scheduled(cron = "0 0 12 * * *")
    @GetMapping
    public void getDailyExchange() {
        RestTemplate restTemplate = new RestTemplate();
        exchangeService.saveDailyRate(restTemplate
                                              .getForEntity(EXCHANGE_API_URL + ACCESS_KEY + BASE_CURRENCY + TARGET_CURRENCY,
                                                            CurrentExchangeRateCommand.class).getBody());
        logger.info("Exchange rates saved successfully.");
    }

}
