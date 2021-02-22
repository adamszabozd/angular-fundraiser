package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.service.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@RequestMapping
public class ExchangeController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Value("${exchange-api-url}")
    private String exchangeApiUrl;

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void getDailyExchange() {
        RestTemplate restTemplate = new RestTemplate();
        exchangeService.saveDailyRate(Objects.requireNonNull(restTemplate
                                                                     .getForEntity(exchangeApiUrl,
                                                                                   CurrentExchangeRateCommand.class).getBody()));
        logger.info("Exchange rates saved successfully.");
    }

}
