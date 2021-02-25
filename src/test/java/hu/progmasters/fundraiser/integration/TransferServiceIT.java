package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.repository.TransferRepository;
import hu.progmasters.fundraiser.service.AccountService;
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
public class TransferServiceIT {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FundService fundService;

    private Long fundId;

    void init() {
        String email = "test@gmail.com";
        AccountRegistrationCommand accountRegistrationCommand = new AccountRegistrationCommand();
        accountRegistrationCommand.setEmail(email);
        accountRegistrationCommand.setUsername("tester");
        accountRegistrationCommand.setPassword("qqqq");
        accountService.create(accountRegistrationCommand);
        FundFormCommand fundFormCommand = new FundFormCommand();
        fundFormCommand.setTitle("Save Forests");
        fundFormCommand.setShortDescription("They are in danger!!!");
        fundFormCommand.setCategory("NONPROFIT");
        fundFormCommand.setTargetAmount(1000000.00);
        fundService.saveNewFund(fundFormCommand, email);
        fundId = fundService.findAll().get(0).getId();
    }

    @Test
    void testSavePendingTransfer() {
        init();
        TransferCreationCommand transferCreationCommand = new TransferCreationCommand();
        transferCreationCommand.setSenderAmount(200.00);
        transferCreationCommand.setTargetFundId(fundId);
        Transfer t = transferService.savePendingTransfer(transferCreationCommand, "fenny26@gmail.com", Locale.getDefault());
        assertEquals(1, transferRepository.count());
        assertFalse(t.getConfirmed());
    }

}
