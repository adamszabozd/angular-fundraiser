package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SharedValidationService {

    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    private final FundRepository fundRepository;

    @Autowired
    public SharedValidationService(AccountRepository accountRepository, TransferRepository transferRepository, FundRepository fundRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.fundRepository = fundRepository;
    }

    public boolean pendingTransferExistsAndSourceIsRight(String confirmationCode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Transfer pendingTransfer = transferRepository.findTransferByConfirmationCodeAndConfirmedFalse(confirmationCode);
        if (pendingTransfer == null) {
            return false;
        } else {
            return pendingTransfer.getSource().getEmail().equals(email);
        }
    }

    public boolean emailAlreadyExists (String email){
        Account account = accountRepository.findByEmail(email);
        return account != null;
    }

    public boolean usernameAlreadyExists (String username){
        Account account = accountRepository.findByUsername(username);
        return account != null;
    }

    public boolean fundTitleAlreadyExist(String title) {
        Fund fund = fundRepository.findByFundTitle(title);
        return fund != null;
    }

    public Integer checkBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountRepository.findByEmail(authentication.getName()).getBalance();
    }

}
