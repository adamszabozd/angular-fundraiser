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

package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.AccountDetails;
import hu.progmasters.fundraiser.dto.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.TransferInitData;
import hu.progmasters.fundraiser.dto.TransferListItem;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.TransferService;
import hu.progmasters.fundraiser.validation.TransferCreationCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private TransferService transferService;
    private AccountService accountService;
    private TransferCreationCommandValidator transferCreationCommandValidator;

    @Autowired
    public TransferController(TransferService transferService, AccountService accountService, TransferCreationCommandValidator transferCreationCommandValidator) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.transferCreationCommandValidator = transferCreationCommandValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(transferCreationCommandValidator);
    }

    @GetMapping
    public ResponseEntity<List<TransferListItem>> getAllTransferListItems() {
        List<TransferListItem> transferItems = transferService.findAll().stream()
                .map(TransferListItem::new).collect(Collectors.toList());
        return new ResponseEntity<>(transferItems, HttpStatus.OK);
    }

    @GetMapping("/newTransferData")
    public ResponseEntity<TransferInitData> newTransferData(HttpServletRequest request) {
        Account myAccount = accountService.findByIpAddress(request.getRemoteAddr());
        List<AccountDetails> otherAccounts = accountService.getAllAccountDetailsExceptOwn(myAccount);
        TransferInitData initData = new TransferInitData(myAccount.getUsername(), otherAccounts, myAccount.getBalance());

        return new ResponseEntity<>(initData, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveTransfer(@Valid @RequestBody TransferCreationCommand transferCreationCommand, HttpServletRequest request) {
        ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
        Transfer transfer = transferService.saveTransfer(transferCreationCommand, request.getRemoteAddr());
        if (transfer == null) {
            logger.warn("Transfer failed, source or target account does not exist!");
            response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
