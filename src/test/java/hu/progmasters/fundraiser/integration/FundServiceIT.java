package hu.progmasters.fundraiser.integration;


import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.fund.FundListItem;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.FundService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
public class FundServiceIT {

    @Autowired
    private FundService fundService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FundRepository fundRepository;


    @Test
    @Disabled
    void testFetchAllForList() {

        AccountRegistrationCommand accountRegistrationCommand = new AccountRegistrationCommand();
        accountRegistrationCommand.setEmail("email@email.com");
        accountRegistrationCommand.setUsername("matyi");
        accountRegistrationCommand.setPassword("yxcv");

        accountService.create(accountRegistrationCommand);

        FundFormCommand fundFormCommand = FundFormCommand.getDummyInstance("test");

        fundService.saveNewFund(fundFormCommand, "email@email.com");

        FundFormCommand fundFormCommand2 = FundFormCommand.getDummyInstance("Test two");

        fundService.saveNewFund(fundFormCommand2, "email@email.com");

        List<FundListItem> fundListItemList = fundService.fetchAllForList(Locale.getDefault());

        Assertions.assertEquals(2, fundListItemList.size());

    }


}
