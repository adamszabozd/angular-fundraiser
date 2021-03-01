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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final TransferRepository transferRepository;
    private final ExchangeService exchangeService;
    private final AccountService accountService;
    private final FundService fundService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendingService emailSendingService;

    @Autowired
    public TransferService(TransferRepository transferRepository,
                           ExchangeService exchangeService, AccountService accountService,
                           FundService fundService, PasswordEncoder passwordEncoder,
                           EmailSendingService emailSendingService
    ) {
        this.transferRepository = transferRepository;
        this.exchangeService = exchangeService;
        this.accountService = accountService;
        this.fundService = fundService;
        this.passwordEncoder = passwordEncoder;
        this.emailSendingService = emailSendingService;
    }

    public Transfer savePendingTransfer(TransferCreationCommand transferCreationCommand, String email, Locale locale) {
        Account source = accountService.findByEmail(email);
        Fund goal = fundService.findById(transferCreationCommand.getTargetFundId());
        Transfer pendingTransfer = new Transfer(transferCreationCommand, source, goal);
        pendingTransfer.setTargetAmount(findCurrencyRate(source, goal) * transferCreationCommand.getSenderAmount());
        String code = getCode();
        logger.info("Confirmation code generated.");
        pendingTransfer.setConfirmationCode(passwordEncoder.encode(code));
        pendingTransfer = transferRepository.save(pendingTransfer);
        emailSendingService.sendConfirmationEmail(email, pendingTransfer.getId(), code, pendingTransfer.getTarget().getFundTitle(), pendingTransfer.getSenderAmount(), pendingTransfer.getSenderCurrency().name(), locale);
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

    public void resendConfirmationEmail(Long id, String email, Locale locale) {
        Transfer pendingTransfer = getPendingTransferByIdAndEmail(id, email);
        String code = getCode();
        pendingTransfer.setConfirmationCode(passwordEncoder.encode(code));
        transferRepository.save(pendingTransfer);
        emailSendingService.sendConfirmationEmail(email, pendingTransfer.getId(), code, pendingTransfer.getTarget().getFundTitle(), pendingTransfer.getSenderAmount(), pendingTransfer.getSenderCurrency().name(), locale);
    }

    private Double findCurrencyRate(Account sender, Fund receiver) {
        Double senderCurrencyRate = exchangeService.findRateByCurrency(sender.getCurrency());
        Double receiverCurrencyRate = exchangeService.findRateByCurrency(receiver.getCurrency());
        return receiverCurrencyRate / senderCurrencyRate;
    }

    public Long confirmTransfer(TransferConfirmationCommand transferConfirmationCommand, String email) {
        Transfer transfer = getOwnTransfer(transferConfirmationCommand.getId(), email);
        if (transfer.getConfirmed()) {
            throw new AlreadyConfirmedTransferException(email);
        }
        if (!passwordEncoder.matches(transferConfirmationCommand.getConfirmationCode(), transfer.getConfirmationCode())) {
            throw new InvalidConfirmationCodeException(email);
        }
        Fund targetFund = transfer.getTarget();
        checkActualSenderCurrency(transfer);
        if (targetFund.getCurrency().name().equals(transfer.getTargetCurrency().name())) {
            transfer.setTargetAmount(findCurrencyRate(transfer.getSource(), targetFund) * transfer.getSenderAmount());
            targetFund.setRaisedAmount(targetFund.getRaisedAmount() + transfer.getTargetAmount());
            Account source = transfer.getSource();
            source.setBalance(source.getBalance() - transfer.getSenderAmount());
            transfer.setTimeStamp(LocalDateTime.now());
            transfer.setConfirmed(true);
            transferRepository.save(transfer);
            return transfer.getId();
        } else {
            throw new InvalidTargetFundException(email);
        }
    }

    private void checkActualSenderCurrency(Transfer transfer) {
        Currency senderRealCurrency = transfer.getSource().getCurrency();
        if (!senderRealCurrency.name().equals(transfer.getSenderCurrency().name())) {
            Double senderRealAmount = exchangeService.findRateByCurrency(senderRealCurrency)
                    / exchangeService.findRateByCurrency(transfer.getSenderCurrency()) * transfer.getSenderAmount();
            transfer.setSenderAmount(senderRealAmount);
            transfer.setSenderCurrency(senderRealCurrency);
        }
    }

    public void deleteTransfer(Long id, String email) {
        Transfer transfer = getOwnTransfer(id, email);
        if (transfer.getConfirmed()) {
            throw new ConfirmedTransferDeletionException(email);
        } else {
            transferRepository.delete(transfer);
        }
    }

    public Transfer getOwnTransfer(Long id, String email) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);
        if (transferOptional.isEmpty()) {
            throw new TransferNotFoundOrNotOwnException(email);
        }
        Transfer transfer = transferOptional.get();
        if (!transfer.getSource().getEmail().equals(email)) {
            throw new TransferNotFoundOrNotOwnException(email);
        }
        return transfer;
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
        Transfer transfer = getOwnTransfer(id, email);
        if (transfer.getConfirmed()) {
            throw new AlreadyConfirmedTransferException(email);
        } else {
            return transfer;
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
