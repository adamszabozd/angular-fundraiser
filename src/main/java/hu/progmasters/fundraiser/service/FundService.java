package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;
    //TODO - Review: Ne hívjuk "keresztbe" a rétegeket, ha elkerülhető, FundService a Fundokért felel,
    // ergo FundRepositoryba nyúljon csak. Amennyiben Account v Transfer kell neki, azt a dedikált Serviceiken keresztül kérjük le inkább
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final MessageSource messageSource;
    private final ExchangeService exchangeService;

    @Autowired
    public FundService(FundRepository fundRepository,
                       AccountService accountService,
                       TransferRepository transferRepository,
                       ExchangeService exchangeService,
                       MessageSource messageSource
    ) {
        this.fundRepository = fundRepository;
        this.accountService = accountService;
        this.exchangeService = exchangeService;
        this.transferRepository = transferRepository;
        this.messageSource = messageSource;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

    public List<FundListItem> fetchAllForList(Locale locale) {
        return fundRepository.findAll().stream()
                             .map(fund -> {
                                 String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                                 return new FundListItem(fund, categoryDisplayName);
                             })
                             .collect(Collectors.toList());
    }

    public void saveNewFund(FundFormCommand fundFormCommand, String emailAddress) {
        Account myAccount = accountService.findByEmail(emailAddress);
        Fund fund = new Fund(fundFormCommand, myAccount);
        myAccount.getFunds().add(fund);
        fundRepository.save(fund);
    }

    public FundDetailsItem fetchFundDetails(Long id, Locale locale) {
        //TODO - Review: A következő két sor kb 3x ismétlődik, csak ebben az osztályban...
        // Sok kicsi sokra megy...
        // Az IllegalArgumentException helyett van beszédesebb: EntityNotFoundException
        Optional<Fund> fund = fundRepository.findById(id);
        if (fund.isPresent()) {
            Long backers = transferRepository.numberOfBackers(id);
            String categoryDisplayName = messageSource.getMessage(fund.get().getFundCategory().getCode(), null, locale);
            return new FundDetailsItem(fund.get(), backers, categoryDisplayName);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<FundListItem> fetchMyFunds(String email, Locale locale) {

        Account myAccount = accountService.findByEmail(email);
        //TODO - Review: Itt miért nem a FundRepository van használva??
        return myAccount.getFunds().stream()
                        .map(fund -> {
                            String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                            return new FundListItem(fund, categoryDisplayName);
                        })
                        .collect(Collectors.toList());
    }

    public void modifyFund(ModifyFundFormCommand modifyFundFormCommand) {
        //TODO - Review: Ugyanaz a kódismétlés... ezt is ki lehet emelni
        Optional<Fund> optionalFund = fundRepository.findById(modifyFundFormCommand.getId());

        if (optionalFund.isPresent()) {
            Fund fund = optionalFund.get();
            fund.setShortDescription(modifyFundFormCommand.getShortDescription());
            fund.setLongDescription(modifyFundFormCommand.getLongDescription());
            fund.setImageUrl(modifyFundFormCommand.getImageUrl());
            fund.setTargetAmount(modifyFundFormCommand.getTargetAmount());
            fund.setEndDate(modifyFundFormCommand.getEndDate());
            fundRepository.save(fund);
        } else {
            throw new IllegalArgumentException("Fund not found by id. Modify unsuccessful");
        }

    }

    public List<FundListItem> fetchFundsByCategory(String categoryName, Locale locale) {
        if (contains(categoryName)) {
            FundCategory category = FundCategory.valueOf(categoryName);
            List<Fund> funds = fundRepository.findAllByCategory(category);
            return funds.stream()
                        .map(fund -> {
                            String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                            return new FundListItem(fund, categoryDisplayName);
                        })
                        .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public FundFormInitData fetchFundFormInitData(Locale locale) {
        FundFormInitData fundFormInitData = new FundFormInitData();
        List<CategoryOption> categoryOptions = getCategoryOptions(locale);
        fundFormInitData.setCategoryOptions(categoryOptions);
        fundFormInitData.setCurrencyOptions(exchangeService.fetchRates());
        return fundFormInitData;
    }

    private boolean contains(String categoryName) {

        for (FundCategory c : FundCategory.values()) {
            if (c.name().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public Fund findById(Long id) {
        Optional<Fund> fundOptional = fundRepository.findById(id);
        if (fundOptional.isPresent()) {
            return fundOptional.get();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<Fund> findAllById(Long id) {
        List<Fund> fundList = new ArrayList<>();
        fundList.add(findById(id));
        return fundList;
    }

    public List<CategoryOption> getCategoryOptions(Locale locale) {
        List<CategoryOption> categoryOptions = new ArrayList<>();
        for (FundCategory category : FundCategory.values()) {
            categoryOptions.add(new CategoryOption(category.toString(), messageSource.getMessage(category.getCode(), null, locale)));
        }
        return categoryOptions;
    }

}
