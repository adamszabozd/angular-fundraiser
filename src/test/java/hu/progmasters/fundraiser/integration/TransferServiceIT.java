package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
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

    public void init() {
        String email = "test@gmail.com";
        AccountRegistrationCommand accountRegistrationCommand = new AccountRegistrationCommand();
        accountRegistrationCommand.setEmail(email);
        accountRegistrationCommand.setUsername("tester");
        accountRegistrationCommand.setPassword("qqqq");
        accountService.create(accountRegistrationCommand);
        FundFormCommand fundFormCommand = new FundFormCommand();
        fundFormCommand.setTitle("Save Forests");
        fundFormCommand.setShortDescription("They are in danger!!!");
        fundFormCommand.setImageUrl("www.forest.com/forest.jpg");
        fundFormCommand.setCategory("NONPROFIT");
        fundFormCommand.setTargetAmount(1000000.00);
        fundService.saveNewFund(fundFormCommand, email);
        fundId = fundService.findAll().get(0).getId();
    }

    @Test
    public void testSavePendingTransfer() {
        init();
        TransferCreationCommand transferCreationCommand = new TransferCreationCommand();
        transferCreationCommand.setAmount(200.00);
        transferCreationCommand.setTargetFundId(fundId);
        Transfer t = transferService.savePendingTransfer(transferCreationCommand, "test@gmail.com");
        assertEquals(transferRepository.count(), 1);
        assertFalse(t.getConfirmed());
    }

}
