package hu.progmasters.fundraiser.integration;

import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.exchange.CurrentExchangeRateCommand;
import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.ExchangeService;
import hu.progmasters.fundraiser.service.FundService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class FundServiceIT {

    @Autowired
    private FundService fundService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private FundRepository fundRepository;

    private static final Locale localeEn = new Locale.Builder().setLanguage("en").build();
    private static final Locale localeHun = new Locale.Builder().setLanguage("hu").build();


    @BeforeEach
     void init() {
        AccountRegistrationCommand regCommand = AccountRegistrationCommand.getDummyInstance("tester@email.com", "tester");
        accountService.create(regCommand);
        CurrentExchangeRateCommand exchangeCommand = CurrentExchangeRateCommand.getDummyData();
        exchangeService.saveDailyRate(exchangeCommand);
        FundFormCommand fundFormCommand = FundFormCommand.getDummyInstance("test1");
        FundFormCommand fundFormCommand2 = FundFormCommand.getDummyInstance("test2");
        FundFormCommand fundFormCommand3 = FundFormCommand.getDummyPassiveFund();
        fundService.saveNewFund("tester@email.com", fundFormCommand);
        fundService.saveNewFund("tester@email.com", fundFormCommand2);
        fundService.saveNewFund("tester@email.com", fundFormCommand3);
        accountService.create(AccountRegistrationCommand.getDummyInstance("othertester@email.com", "tester2"));
        fundService.saveNewFund("othertester@email.com", FundFormCommand.getDummyInstance("title"));
        fundService.saveNewFund("tester@email.com", FundFormCommand.getDummyInstance("test4"));
        fundService.saveNewFund("tester@email.com", FundFormCommand.getDummyInstance("test5"));
        fundService.saveNewFund("tester@email.com", FundFormCommand.getDummyInstance("test6"));
        fundService.saveNewFund("tester@email.com", FundFormCommand.getDummyInstance("test7"));
        fundService.saveNewFund("tester@email.com", FundFormCommand.getDummyInstance("test8"));
    }


    @Test
    void testSaveNewFund(){
        assertEquals(9, fundRepository.count());
    }

    @Test
    void testFetchActiveTargetFunds() {
        assertEquals(9, fundRepository.count());
        assertEquals(8, fundService.fetchActiveTargetFunds().size());
    }

    @Test
    void testGetCategoryOptionsEn(){
        List<CategoryOption> categoryOptions = fundService.getCategoryOptions(localeEn);
        assertEquals(4, categoryOptions.size());
        assertEquals("Medical", categoryOptions.get(0).getDisplayName());
    }

    @Test
    void testGetCategoryOptionsHun() {
        List<CategoryOption> categoryOptions = fundService.getCategoryOptions(localeHun);
        assertEquals(4, categoryOptions.size());
        assertEquals("Egészségügy", categoryOptions.get(0).getDisplayName());

    }

    @Test
    void testFetchFundFormInitData(){
        FundFormInitData initData = fundService.fetchFundFormInitData(localeEn);
        assertEquals(4, initData.getCategoryOptions().size());
        assertEquals(3, initData.getStatusOptions().size());
        assertEquals(4, initData.getCurrencyOptions().size());
        assertEquals("Medical", initData.getCategoryOptions().get(0).getDisplayName());
        assertEquals("Active", initData.getStatusOptions().get(0).getDisplayName());
        assertEquals("EUR", initData.getCurrencyOptions().get(0).getCurrencyName());
    }

    @Test
    void testFetchMyFunds(){

        assertEquals(9, fundRepository.count());
        assertEquals(8, fundService.fetchMyFunds("tester@email.com", localeEn).size());

    }

    @Test
    void testFetchFundDetails(){
        FundDetailsItem details = fundService.fetchFundDetails(1L, localeEn);
        assertEquals("test1", details.getTitle());
    }

    @Test
    void testFetchMyFundDetails(){
        FundDetailsItem details = fundService.fetchMyFundDetails("tester@email.com", 1L, localeEn);
        assertEquals("test1", details.getTitle());
    }

    @Test
    void TestFillModifyFundForm(){
        ModifyFundFormInit formInit = fundService.fillModifyFundForm("tester@email.com", 3L, localeEn);
        assertEquals("Passive Fund", formInit.getTitle());
        assertEquals("PASSIVE", formInit.getStatus());
    }

    @Test
    void testModifyFund(){
        ModifyFundFormCommand command = ModifyFundFormCommand.getDummyModifyCommand(1L);
        fundService.modifyFund("tester@email.com", command);
        FundDetailsItem details = fundService.fetchFundDetails(1L, localeEn);
        assertEquals("Modified short description", details.getShortDescription());
    }

    @Test
    void testFetchPageableList() {


        PageRequest pageInformation = PageRequest.of(0, 6);
        FundPageData data = fundService.fetchPageableList(pageInformation, null, localeEn);
        assertEquals(8L, data.getCount());
        assertEquals(6, data.getFunds().size());

    }

}
