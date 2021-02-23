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
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.transfer.create.TransferConfirmationCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.transfer.list.MyTransferListPendingItem;
import hu.progmasters.fundraiser.exception.*;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);
    //TODO - Review: Ne hívjuk "keresztbe" a rétegeket. Ugyanaz mint a FundServiceben
    private final TransferRepository transferRepository;
    private final ExchangeService exchangeService;
    private final AccountService accountService;
    private final FundService fundService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TransferService(TransferRepository transferRepository,
                           ExchangeService exchangeService, AccountService accountService,
                           FundService fundService, PasswordEncoder passwordEncoder
    ) {
        this.transferRepository = transferRepository;
        this.exchangeService = exchangeService;
        this.accountService = accountService;
        this.fundService = fundService;
        this.passwordEncoder = passwordEncoder;
    }

    public Transfer savePendingTransfer(TransferCreationCommand transferCreationCommand, String email) {
        Account source = accountService.findByEmail(email);
        Fund goal = fundService.findById(transferCreationCommand.getTargetFundId());
        return getPendingTransfer(transferCreationCommand, source, goal);
    }

    private Transfer getPendingTransfer(TransferCreationCommand transferCreationCommand, Account source, Fund goal) {
        Transfer pendingTransfer = new Transfer(transferCreationCommand, source, goal);
        pendingTransfer.setTargetAmount(calculateExchangedAmount(findCurrencyRate(source, goal), transferCreationCommand.getSenderAmount()));
        String code = getCode();
        logger.info("Confirmation code generated.");
        pendingTransfer.setConfirmationCode(passwordEncoder.encode(getCode()));
        pendingTransfer.setUnencryptedConfirmationCode(code);
        pendingTransfer = transferRepository.save(pendingTransfer);
        return pendingTransfer;
    }

    private String getCode() {
        boolean codeGenerated = false;
        String code = null;
        while (!codeGenerated) {
            code = RandomStringUtils.random(10, true, true);
            if (!isPendingConfirmationCode(code)) {
                codeGenerated = true;
            }
        }
        return code;
    }

    public void generateNewConfirmationCode(Transfer transfer) {
        String code = getCode();
        transfer.setConfirmationCode(passwordEncoder.encode(code));
        transfer.setUnencryptedConfirmationCode(code);
        transferRepository.save(transfer);
    }

    private Double findCurrencyRate(Account sender, Fund receiver) {
        Double senderCurrencyRate = exchangeService.findRateByCurrency(sender.getCurrency());
        Double receiverCurrencyRate = exchangeService.findRateByCurrency(receiver.getCurrency());
        return receiverCurrencyRate / senderCurrencyRate;
    }

    private Double calculateExchangedAmount(Double exchangeRate, Double senderAmount) {
        return (exchangeRate * senderAmount);
    }

    public void confirmTransfer(TransferConfirmationCommand transferConfirmationCommand, String email) {
        String rawCode = transferConfirmationCommand.getConfirmationCode();
        List<Transfer> pendingTransfers = transferRepository.getPendingTransfers();
        Transfer transfer = null;
        for (Transfer pendingTransfer : pendingTransfers) {
            if (passwordEncoder.matches(rawCode, pendingTransfer.getConfirmationCode())) {
                transfer = pendingTransfer;
                break;
            }
        }
        if (transfer == null) {
            throw new InvalidConfirmationCodeException(email);
        }
        Fund goal = transfer.getTarget();
        Currency senderCurrency = transfer.getSource().getCurrency();
        if (!senderCurrency.name().equals(transfer.getSenderCurrency().name())) {
            transfer.setSenderAmount(calculateExchangedAmount(findCurrencyRate(transfer.getSource(), goal), transfer.getSenderAmount()));
            transfer.setSenderCurrency(senderCurrency);
        }
        if (goal.getCurrency().name().equals(transfer.getTargetCurrency().name())) {
            transfer.setTargetAmount(calculateExchangedAmount(findCurrencyRate(transfer.getSource(), goal), transfer.getSenderAmount()));
            goal.setRaisedAmount(goal.getRaisedAmount() + transfer.getTargetAmount());
            Account source = transfer.getSource();
            source.setBalance(source.getBalance() - transfer.getSenderAmount());
            transfer.setTimeStamp(LocalDateTime.now());
            transfer.setConfirmed(true);
            transferRepository.save(transfer);
        } else {
            throw new InvalidTargetFundException(email);
        }
    }

    public void deleteTransfer(Long id, String email) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);
        if (transferOptional.isEmpty()) {
            throw new TransferNotFoundException(email);
        }
        Transfer transfer = transferOptional.get();
        if (transfer.getConfirmed()) {
            throw new ConfirmedTransferDeleteException(email);
        } else if (transfer.getSource().getEmail().equals(email)) {
            transferRepository.delete(transfer);
        } else {
            throw new NotOwnTransferException(email);
        }
    }

    public List<MyTransferListPendingItem> getPendingTransfers(String email) throws AccountNotFoundException {
        Account account = accountService.findByEmail(email);
        if (account != null) {
            return account.getOutgoingTransfers().stream()
                          .filter(t -> !t.getConfirmed())
                          .map(MyTransferListPendingItem::new)
                          .collect(Collectors.toList());
        } else {
            throw new AccountNotFoundException();
        }
    }

    public Transfer getPendingTransferByIdAndEmail(Long id, String email) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);
        if (transferOptional.isEmpty()) {
            throw new TransferNotFoundException(email);
        }
        Transfer transfer = transferOptional.get();
        if (transfer.getConfirmed()) {
            throw new AlreadyConfirmedTransferException(email);
        } else if (transfer.getSource().getEmail().equals(email)) {
            return transfer;
        } else {
            throw new NotOwnTransferException(email);
        }
    }

    public boolean isPendingConfirmationCode(String rawCode) {
        List<Transfer> pendingTransfers = transferRepository.getPendingTransfers();
        for (Transfer pendingTransfer : pendingTransfers) {
            if (passwordEncoder.matches(rawCode, pendingTransfer.getConfirmationCode())) {
                return true;
            }
        }
        return false;
    }

}
