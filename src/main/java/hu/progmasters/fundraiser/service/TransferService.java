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
import hu.progmasters.fundraiser.dto.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.TransferListItem;
import hu.progmasters.fundraiser.repository.AccountRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransferService {

    private TransferRepository transferRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public Transfer saveTransfer(TransferCreationCommand transferCreationCommand, String ipAddress) {
        Account source = accountRepository.findByIpAddress(ipAddress);
        Account target = accountRepository.findById(transferCreationCommand.getTarget()).orElse(null);
        Transfer transfer = null;

        if (source != null && target != null) {
            transfer = new Transfer();
            transfer.setTimeStamp(LocalDateTime.now());
            transfer.setAmount(transferCreationCommand.getAmount());

            target.setFunds(target.getFunds() + transferCreationCommand.getAmount());
            transfer.setTarget(target);

            source.setBalance(source.getBalance() - transferCreationCommand.getAmount());
            transfer.setSource(source);
            transfer = transferRepository.save(transfer);
        }

        return transfer;
    }

    public List<Transfer> findAll() {
        return transferRepository.findAllByOrderByTimeStampDesc();
    }

    public List<TransferListItem> getMySourceTransfers(Account account) {
        List<TransferListItem> transferListItems = new ArrayList<>();
        for (Transfer transfer : transferRepository.findAllBySourceOrderByTimeStampDesc(account)) {
            transferListItems.add(new TransferListItem(transfer));
        }
        return transferListItems;
    }

    public List<TransferListItem> getMyTargetTransfers(Account account) {
        List<TransferListItem> transferListItems = new ArrayList<>();
        for (Transfer transfer : transferRepository.findAllByTargetOrderByTimeStampDesc(account)) {
            transferListItems.add(new TransferListItem(transfer));
        }
        return transferListItems;
    }
}
