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
import hu.progmasters.fundraiser.dto.AccountDetails;
import hu.progmasters.fundraiser.dto.AccountRegistrationCommand;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.TransferService;
import hu.progmasters.fundraiser.validation.AccountRegistrationCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;
    private TransferService transferService;
    private AccountRegistrationCommandValidator accountRegistrationCommandValidator;

    @Autowired
    public AccountController(AccountService accountService, TransferService transferService, AccountRegistrationCommandValidator accountRegistrationCommandValidator) {
        this.accountService = accountService;
        this.transferService = transferService;
        this.accountRegistrationCommandValidator = accountRegistrationCommandValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(accountRegistrationCommandValidator);
    }

    @GetMapping
    public ResponseEntity<List<AccountDetails>> getAllAccounts() {
        List<AccountDetails> accountDetails = new ArrayList<>();
        List<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            accountDetails.add(new AccountDetails(account));
        }
        Collections.sort(accountDetails);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);
    }

    @GetMapping("/myAccountDetails")
    public ResponseEntity<AccountDetails> getMyAccountDetails(HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();
        Account myAccount = accountService.findMyAccount(ipAddress);
        if (myAccount != null) {
            AccountDetails myAccountDetails = new AccountDetails(myAccount);
            myAccountDetails.setSourceTransfers(transferService.getMySourceTransfers(myAccount));
            myAccountDetails.setTargetTransfers(transferService.getMyTargetTransfers(myAccount));
            return new ResponseEntity<>(myAccountDetails, HttpStatus.OK);
        } else {
            logger.debug("Account not found for IP address: " + ipAddress);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity registerNewAccount(@RequestBody @Valid AccountRegistrationCommand accountRegistrationCommand,
                                             HttpServletRequest request) {
        accountService.create(new Account(accountRegistrationCommand), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
