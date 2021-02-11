package hu.progmasters.fundraiser;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
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
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FundRepository fundRepository;

    private Long accountId;
    private Long fundId;

    public void init() {
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setUsername("tester");
        account.setBalance(5000);
        account.setPassword("qqqq");
        accountId = accountRepository.save(account).getId();
        Fund fund = new Fund();
        fund.setFundTitle("Save Forests");
        fundId = fundRepository.save(fund).getId();
    }

    @Test
    public void testSavePendingTransfer() {
        init();
        TransferCreationCommand transferCreationCommand = new TransferCreationCommand();
        transferCreationCommand.setAmount(200);
        transferCreationCommand.setTargetFundId(fundId);
        Transfer t = transferService.savePendingTransfer(transferCreationCommand, "test@gmail.com");
        assertEquals(transferRepository.count(), 1);
        assertFalse(t.getConfirmed());
    }

}
