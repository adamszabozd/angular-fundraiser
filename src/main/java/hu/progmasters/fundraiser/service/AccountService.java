/*
 * Copyright © Progmasters (QTC Kft.), 2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Currency;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.account.AccountDetails;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.account.BalanceFormCommand;
import hu.progmasters.fundraiser.dto.account.DonationPerFund;
import hu.progmasters.fundraiser.dto.exchange.CurrencyFormCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExchangeService exchangeService;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, ExchangeService exchangeService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeService = exchangeService;
    }

    public long create(AccountRegistrationCommand command) {
        String encryptedPassword = passwordEncoder.encode(command.getPassword());
        Account savedAccount = accountRepository.save(new Account(command, encryptedPassword));
        return savedAccount.getId();
    }

    public Account findById(Long userId) {
        return accountRepository
                .findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Account findByEmail(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public AccountDetails addDetailsByEmail(String email) {
        Account account = findByEmail(email);
        return new AccountDetails(account, getDonationsPerFund(account));
    }

    public List<DonationPerFund> getDonationsPerFund(Account account) {
        return account.getOutgoingTransfers().stream()
                .filter(Transfer::getConfirmed)
                .collect(Collectors.toMap((Transfer t) -> t.getTarget().getFundTitle(),
                        (Transfer t) -> getTransferAmountByCurrency(t, account.getCurrency()),
                        Double::sum))
                .entrySet().stream()
                .map(entry -> new DonationPerFund(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public double getTransferAmountByCurrency(Transfer transfer, Currency currency) {
        if (transfer.getSenderCurrency() == currency) {
            return transfer.getSenderAmount();
        } else if (transfer.getTargetCurrency() == currency) {
            return transfer.getTargetAmount();
        } else {
            double toRate = exchangeService.findHistoricalRateByCurrency(currency, transfer.getTimeStamp());
            double fromRate = exchangeService.findHistoricalRateByCurrency(transfer.getSenderCurrency(), transfer.getTimeStamp());
            return transfer.getSenderAmount() / fromRate * toRate;
        }
    }

    public AccountDetails fillMyBalance(BalanceFormCommand balanceFormCommand, String email) {
        Account account = findByEmail(email);
        double newBalance = account.getBalance() + balanceFormCommand.getAddAmount();
        account.setBalance(newBalance);
        return new AccountDetails(accountRepository.save(account), getDonationsPerFund(account));
    }

    public AccountDetails savNewCurrency(CurrencyFormCommand currencyFormCommand, String email) {
        Account account = findByEmail(email);
        double oldExchangeRate = exchangeService.findRateByCurrency(account.getCurrency());
        Currency newCurrency = Currency.valueOf(currencyFormCommand.getNewCurrency());
        double newExchangeRate = exchangeService.findRateByCurrency(newCurrency);
        double newBalance = newExchangeRate / oldExchangeRate * account.getBalance();
        account.setBalance(newBalance);
        account.setCurrency(newCurrency);
        return new AccountDetails(accountRepository.save(account), getDonationsPerFund(account));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

}
