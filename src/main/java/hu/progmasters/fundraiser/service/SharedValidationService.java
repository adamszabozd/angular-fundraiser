package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.ReCaptchaResponse;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SharedValidationService {

    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    private final FundRepository fundRepository;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;


    @Autowired
    public SharedValidationService(AccountRepository accountRepository, TransferRepository transferRepository, FundRepository fundRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.fundRepository = fundRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public boolean pendingTransferExistsAndSourceIsRight(String confirmationCode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Transfer> pendingTransfers = transferRepository.getPendingTransfers();
        Transfer pendingTransfer = null;
        for (Transfer transfer : pendingTransfers) {
            if (passwordEncoder.matches(confirmationCode, transfer.getConfirmationCode())) {
                pendingTransfer = transfer;
            }
        }
        if (pendingTransfer == null) {
            return false;
        } else {
            return pendingTransfer.getSource().getEmail().equals(email);
        }
    }

    public boolean emailAlreadyExists(String email) {
        boolean alreadyExist = false;
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isPresent()) {
            alreadyExist = true;
        }
        return alreadyExist;
    }

    public boolean usernameAlreadyExists(String username) {
        Account account = accountRepository.findByUsername(username);
        return account != null;
    }

    public boolean fundTitleAlreadyExist(String title) {
        boolean alreadyExist = false;
        Optional<Fund> optionalFund = fundRepository.findByFundTitle(title);
        if (optionalFund.isPresent()) {
            alreadyExist = true;
        }
        return alreadyExist;
    }

    public Double checkBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountRepository.findByEmail(authentication.getName()).get().getBalance();
    }

    public boolean existsFundById(Long id) {
        return fundRepository.existsById(id);
    }

    public boolean validateCaptcha(String captcha) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LeIKWQaAAAAAH0ok5g2-wWn7U2AE69oHgz4lOOQ\n&response=" + captcha;
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url + params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();
        return reCaptchaResponse != null && reCaptchaResponse.isSuccess();
    }
}
