package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.dto.fund.FundListItem;
import hu.progmasters.fundraiser.dto.fund.ModifyFundFormCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public FundService(FundRepository fundRepository, AccountRepository accountRepository) {
        this.fundRepository = fundRepository;
        this.accountRepository = accountRepository;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

    public List<FundListItem> fetchAllForList(){
        return fundRepository.findAll().stream().map(FundListItem::new).collect(Collectors.toList());
    }


    public void saveNewFund(FundFormCommand fundFormCommand, String emailAddress) {
        Account myAccount = accountRepository.findByEmail(emailAddress);
        Fund fund = new Fund(fundFormCommand, myAccount);
        myAccount.getFunds().add(fund);
        fundRepository.save(fund);
    }

    public FundListItem fetchFundDetails(Long id) {
        Optional<Fund> fund = fundRepository.findById(id);
        if(fund.isPresent()){
            return new FundListItem(fund.get());
        } else throw new IllegalArgumentException();
    }

    public List<FundListItem> fetchMyFunds(String email) {

        Account myAccount = accountRepository.findByEmail(email);
        return myAccount.getFunds().stream().map(FundListItem::new).collect(Collectors.toList());
    }

    public void modifyFund(ModifyFundFormCommand modifyFundFormCommand) {

        Optional<Fund> optionalFund = fundRepository.findById(modifyFundFormCommand.getId());

        if(optionalFund.isPresent()){
            Fund fund = optionalFund.get();
            fund.setShortDescription(modifyFundFormCommand.getShortDescription());
            fund.setLongDescription(modifyFundFormCommand.getLongDescription());
            fund.setImageUrl(modifyFundFormCommand.getImageUrl());
            fund.setTargetAmount(modifyFundFormCommand.getTargetAmount());
            fund.setEndDate(modifyFundFormCommand.getEndDate());
            fundRepository.save(fund);
        }else{
            throw new IllegalArgumentException("Fund not found by id. Modify unsuccessful");
        }

    }

    public List<FundListItem> fetchFundsByCategory(String categoryName) {
        if (contains(categoryName)) {
            FundCategory category = FundCategory.valueOf(categoryName);
            List<Fund> funds = fundRepository.findAllByCategory(category);
            return funds.stream().map(FundListItem::new).collect(Collectors.toList());
        } else throw new IllegalArgumentException();
    }

    private boolean contains(String categoryName) {

        for (FundCategory c : FundCategory.values()) {
            if (c.name().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }
}
