package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.repository.TransferRepository;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.ExchangeService;
import hu.progmasters.fundraiser.service.FundService;
import hu.progmasters.fundraiser.service.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
class TransferServiceIT {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FundService fundService;

    @Autowired
    private ExchangeService exchangeService;

    private Long fundId;

    void init() {
        String email = "test@gmail.com";
        AccountRegistrationCommand funder = AccountRegistrationCommand.getDummyInstance(email);
        accountService.create(funder);
        FundFormCommand fundFormCommand = FundFormCommand.getDummyInstance("Title test");
        fundService.saveNewFund(email, fundFormCommand);
        fundId = fundService.fetchActiveTargetFunds().get(0).getId();
        CurrentExchangeRateCommand currentExchange = CurrentExchangeRateCommand.getDummyData();
        exchangeService.saveDailyRate(currentExchange);
    }

    @Test
    void testSavePendingTransfer() {
        init();
        TransferCreationCommand transferCreationCommand = TransferCreationCommand.getDummyInstance(fundId);
        Transfer t = transferService.savePendingTransfer(transferCreationCommand, "test@gmail.com", Locale.getDefault());
        assertEquals(1, transferRepository.count());
        assertFalse(t.getConfirmed());
    }

}
