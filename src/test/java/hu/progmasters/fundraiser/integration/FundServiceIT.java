package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.dto.FundFormCommand;
import hu.progmasters.fundraiser.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
public class FundServiceIT {

    @Autowired
    private FundService fundService;

    public void testSaveNewFund() {

        FundFormCommand fundFormCommand = new FundFormCommand();
        fundFormCommand.setCategory("Education");
        fundFormCommand.setEndDate(LocalDate.of(2020, 3, 25));
        fundFormCommand.setImageUrl("image.pdf");
        fundFormCommand.setLongDescription("long description");
        fundFormCommand.setShortDescription("short description");
        fundFormCommand.setTargetAmount(2000);
        fundFormCommand.setTitle("Title");

    }

}
