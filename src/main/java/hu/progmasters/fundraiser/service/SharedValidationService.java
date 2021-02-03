package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SharedValidationService {

    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    @Autowired
    public SharedValidationService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public boolean pendingTransferExistsAndSourceIsRight(String confirmationCode, String ipAddress) {
        Transfer pendingTransfer = transferRepository.findTransferByConfirmationCodeAndConfirmedFalse(confirmationCode);
        if (pendingTransfer == null) {
            return false;
        } else {
            return true; // pendingTransfer.getSource().getIpAddress().equals(ipAddress);
        }
    }
}
