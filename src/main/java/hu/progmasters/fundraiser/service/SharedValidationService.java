package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.PendingTransfer;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.PendingTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SharedValidationService {

    private final AccountRepository accountRepository;

    private final PendingTransferRepository pendingTransferRepository;

    @Autowired
    public SharedValidationService(AccountRepository accountRepository, PendingTransferRepository pendingTransferRepository) {
        this.accountRepository = accountRepository;
        this.pendingTransferRepository = pendingTransferRepository;
    }

    public Account getAccountByIpAddress(String ipAddress) {
        return accountRepository.findByIpAddress(ipAddress);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public boolean pendingTransferExistsAndSourceIsRight(String confirmationCode, String ipAddress) {
        PendingTransfer pendingTransfer = pendingTransferRepository.findPendingTransferByConfirmationCode(confirmationCode);
        if (pendingTransfer == null) {
            return false;
        } else {
            return pendingTransfer.getSource().getIpAddress().equals(ipAddress);
        }
    }
}
