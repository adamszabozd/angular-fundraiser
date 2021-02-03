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
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.TransferConfirmationCommand;
import hu.progmasters.fundraiser.dto.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.TransferListItem;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class TransferService {

    Logger logger = LoggerFactory.getLogger(TransferService.class);

    private TransferRepository transferRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public Transfer savePendingTransfer(TransferCreationCommand transferCreationCommand, String ipAddress) {
        Account source = accountRepository.findByEmail(ipAddress);
        Account target = accountRepository.findById(transferCreationCommand.getTarget()).orElse(null);
        Transfer pendingTransfer = null;

        if (source != null && target != null) {
            pendingTransfer = new Transfer();
            pendingTransfer.setAmount(transferCreationCommand.getAmount());

            pendingTransfer.setTarget(target);

            pendingTransfer.setSource(source);

            boolean codeGenerated = false;
            String code = null;
            while (!codeGenerated) {
                code = generateConfirmationCode();
                if (!transferRepository.existsTransfersByConfirmationCodeAndConfirmedFalse(code)) {
                    codeGenerated = true;
                }
            }
            logger.info("Confirmation code generated: {}", codeGenerated);
            pendingTransfer.setConfirmationCode(code);
            pendingTransfer.setConfirmed(false);
            pendingTransfer = transferRepository.save(pendingTransfer);
        }

        return pendingTransfer;
    }

    public Transfer confirmTransfer(TransferConfirmationCommand transferConfirmationCommand) {
        Transfer transfer = transferRepository
                .findTransferByConfirmationCodeAndConfirmedFalse(transferConfirmationCommand.getConfirmationCode());
        if (transfer != null) {
            transfer.setTimeStamp(LocalDateTime.now());

            Account target = transfer.getTarget();
      //      target.setFunds(target.getFunds() + transfer.getAmount());

            Account source = transfer.getSource();
            source.setBalance(source.getBalance() - transfer.getAmount());
            transfer.setSource(source);

            transfer.setConfirmed(true);
            transferRepository.save(transfer);
        }
        return transfer;
    }

    public List<Transfer> findAll() {
        return transferRepository.findAllByConfirmedTrueOrderByTimeStampDesc();
    }

    public List<TransferListItem> getMyOutgoingTransfers(Account account) {
        List<TransferListItem> transferListItems = new ArrayList<>();
        for (Transfer transfer : transferRepository.findAllBySourceAndConfirmedTrueOrderByTimeStampDesc(account)) {
            transferListItems.add(new TransferListItem(transfer));
        }
        return transferListItems;
    }

    public List<TransferListItem> getMyIncomingTransfers(Account account) {
        List<TransferListItem> transferListItems = new ArrayList<>();
        for (Transfer transfer : transferRepository.findAllByTargetAndConfirmedTrueOrderByTimeStampDesc(account)) {
            transferListItems.add(new TransferListItem(transfer));
        }
        return transferListItems;
    }

    /* Generate a random 10 characters long alphanumeric string.
     * The ASCII codes of digits are 48-57,
     *  uppercase English letters 65-90, lowercase English letters 97-122.
     */
    private String generateConfirmationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int ch = 48 + random.nextInt(10 + 26 + 26);
            if (ch > 57) {
                ch += 7;
            }
            if (ch > 90) {
                ch += 6;
            }
            code.append((char)ch);
        }
        return code.toString();
    }
}
