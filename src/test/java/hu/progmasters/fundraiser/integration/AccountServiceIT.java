package hu.progmasters.fundraiser.integration;


import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

//TODO - Review: Nem vittétek túlzásba a teszt írást :D
@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
public class AccountServiceIT {

    @Autowired
    private AccountService accountService;

    @Test
    public void testCreateAccount(){

        AccountRegistrationCommand accountRegistrationCommand = new AccountRegistrationCommand();
        accountRegistrationCommand.setEmail("valaki@gmail.com");
        accountRegistrationCommand.setUsername("Valaki");
        accountRegistrationCommand.setPassword("123123");

        Long  newAccountId = accountService.create(accountRegistrationCommand);
        Account newAccount = accountService.findById(newAccountId);

        Assertions.assertEquals("Valaki",newAccount.getUsername());
        Assertions.assertEquals("valaki@gmail.com",newAccount.getEmail());
        Assertions.assertNotEquals("123123", newAccount.getPassword());

    }

}
