package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.fund.FundDetailsItem;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.fund.FundListItem;
import hu.progmasters.fundraiser.dto.fund.ModifyFundFormCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;
    //TODO - Review: Ne hívjuk "keresztbe" a rétegeket, ha elkerülhető, FundService a Fundokért felel,
    // ergo FundRepositoryba nyúljon csak. Amennyiben Account v Transfer kell neki, azt a dedikált Serviceiken keresztül kérjük le inkább
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Autowired
    public FundService(FundRepository fundRepository, AccountRepository accountRepository, TransferRepository transferRepository) {
        this.fundRepository = fundRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

    public List<FundListItem> fetchAllForList() {
        return fundRepository.findAll().stream().map(FundListItem::new).collect(Collectors.toList());
    }

    public void saveNewFund(FundFormCommand fundFormCommand, String emailAddress) {
        Account myAccount = accountRepository.findByEmail(emailAddress);
        Fund fund = new Fund(fundFormCommand, myAccount);
        myAccount.getFunds().add(fund);
        fundRepository.save(fund);
    }

    public FundDetailsItem fetchFundDetails(Long id) {
        //TODO - Review: A következő két sor kb 3x ismétlődik, csak ebben az osztályban...
        // Sok kicsi sokra megy...
        // Az IllegalArgumentException helyett van beszédesebb: EntityNotFoundException
        Optional<Fund> fund = fundRepository.findById(id);
        if (fund.isPresent()) {
            Long backers = transferRepository.numberOfBackers(id);
            return new FundDetailsItem(fund.get(), backers);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<FundListItem> fetchMyFunds(String email) {

        Account myAccount = accountRepository.findByEmail(email);
        //TODO - Review: Itt miért nem a FundRepository van használva??
        return myAccount.getFunds().stream().map(FundListItem::new).collect(Collectors.toList());
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

    public List<FundListItem> fetchFundsByCategory(String categoryName) {
        if (contains(categoryName)) {
            FundCategory category = FundCategory.valueOf(categoryName);
            List<Fund> funds = fundRepository.findAllByCategory(category);
            return funds.stream().map(FundListItem::new).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean contains(String categoryName) {

        for (FundCategory c : FundCategory.values()) {
            if (c.name().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public List<Fund> findById(Long id) {
        Optional<Fund> fundOptional = fundRepository.findById(id);
        if (fundOptional.isPresent()) {
            Fund fund = fundOptional.get();
            List<Fund> fundList = new ArrayList<>();
            fundList.add(fund);
            return fundList;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
