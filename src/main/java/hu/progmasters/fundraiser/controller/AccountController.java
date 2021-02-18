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
import hu.progmasters.fundraiser.dto.account.AccountDetails;
import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.account.AuthenticatedAccountDetails;
import hu.progmasters.fundraiser.dto.account.BalanceFormCommand;
import hu.progmasters.fundraiser.dto.exchange.CurrencyOption;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.ExchangeService;
import hu.progmasters.fundraiser.validation.AccountRegistrationCommandValidator;
import hu.progmasters.fundraiser.validation.BalanceFormCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final ExchangeService exchangeService;
    private final AccountRegistrationCommandValidator accountRegistrationCommandValidator;
    private final BalanceFormCommandValidator balanceFormCommandValidator;

    @Autowired
    public AccountController(
            AccountService accountService,
            ExchangeService exchangeService, AccountRegistrationCommandValidator accountRegistrationCommandValidator,
            BalanceFormCommandValidator balanceFormCommandValidator
    ) {
        this.accountService = accountService;
        this.exchangeService = exchangeService;
        this.accountRegistrationCommandValidator = accountRegistrationCommandValidator;
        this.balanceFormCommandValidator = balanceFormCommandValidator;
    }

    @InitBinder("accountRegistrationCommand")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(accountRegistrationCommandValidator);
    }

    @InitBinder("balanceFormCommand")
    protected void initBinderForAmount(WebDataBinder binder) {
        binder.addValidators(balanceFormCommandValidator);
    }

    @PostMapping
    public ResponseEntity<Void> registerNewAccount(
            @RequestBody @Valid AccountRegistrationCommand accountRegistrationCommand
    ) {
        long newAccountId = accountService.create(accountRegistrationCommand);
        logger.info("User '" + newAccountId + "' successfully registered!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/myAccountDetails")
    public ResponseEntity<AccountDetails> getMyAccountDetails(Principal principal) {
        //TODO - Review: A konverzió is üzleti logikának minősül, ne itt csináljuk ezeket
        // Más, ha csak a SecurityContextből húzunk ki valamit, de itt lemegyünk egészen az adatbázisig
        Account myAccount = accountService.findByEmail(principal.getName());
        AccountDetails myAccountDetails = new AccountDetails(myAccount);

        return new ResponseEntity<>(myAccountDetails, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedAccountDetails> getMyAccountDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(new AuthenticatedAccountDetails(user), HttpStatus.OK);
    }

    @GetMapping("/loggedIn")
    public ResponseEntity<Boolean> isLoggedIn(Principal principal) {
        return new ResponseEntity<>(principal != null, HttpStatus.OK);
    }

    @PutMapping("/change/balance")
    public ResponseEntity<AccountDetails> fillMyBalance(@RequestBody @Valid BalanceFormCommand balanceFormCommand,
                                                        Principal principal
    ) {
        AccountDetails myAccountDetails = accountService.fillMyBalance(balanceFormCommand, principal.getName());
        logger.info("Balance  successfully filled with" + balanceFormCommand.getAddAmount());
        return new ResponseEntity(myAccountDetails, HttpStatus.CREATED);
    }

    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyOption>> getCurrencies(Principal principal) {
        return new ResponseEntity(exchangeService.fetchRates(), HttpStatus.OK);
    }

    // Ezt max akkor fogjuk használni, ha csinálunk adminfelületet is. Mezei usereknek nem listázzuk ki az összes regisztáltat.
    @GetMapping
    public ResponseEntity<List<AccountDetails>> getAllAccounts() {
        List<AccountDetails> accountDetails = accountService.findAll().stream()
                                                            .map(AccountDetails::new)
                                                            .sorted()
                                                            .collect(Collectors.toList());
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);
    }

}
