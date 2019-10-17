package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SharedValidationService {

    private final AccountRepository accountRepository;

    @Autowired
    public SharedValidationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByIpAddress(String ipAddress) {
        return accountRepository.findByIpAddress(ipAddress);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
