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

import hu.progmasters.fundraiser.dto.AccountDetails;
import hu.progmasters.fundraiser.dto.AccountRegistrationCommand;
import hu.progmasters.fundraiser.dto.AuthenticatedAccountDetails;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.validation.AccountRegistrationCommandValidator;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static hu.progmasters.fundraiser.config.SpringWebConfig.SESSION_USER_ID_KEY;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final AccountRegistrationCommandValidator accountRegistrationCommandValidator;

    @Autowired
    public AccountController(
            AccountService accountService,
            AccountRegistrationCommandValidator accountRegistrationCommandValidator
    ) {
        this.accountService = accountService;
        this.accountRegistrationCommandValidator = accountRegistrationCommandValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(accountRegistrationCommandValidator);
    }

    @PostMapping
    public ResponseEntity<Void> registerNewAccount(HttpSession httpSession,
                                                   @RequestBody @Valid AccountRegistrationCommand accountRegistrationCommand
    ) {
        long newAccountId = accountService.create(accountRegistrationCommand);
        httpSession.setAttribute(SESSION_USER_ID_KEY, newAccountId);
        logger.info("User '" + newAccountId + "' successfully registered!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/myAccountDetails")
    public ResponseEntity<AccountDetails> getMyAccountDetails(Principal principal, HttpSession httpSession) {
        //TODO Ha itt sessiont használunk, van szükség principalra?
        //TODO - REVIEW: Session-re nincs szükség ;) Ha szükséged van a UserID-ra, azt be lehet rakni a principalbe,
        // ez nem túl triviális, de szívesen megmutatom
        Long userId = (Long) httpSession.getAttribute(SESSION_USER_ID_KEY);

        boolean userExistsInSession = userId != null;
        if (userExistsInSession) {
            logger.info("User '" + userId + "' requested own details");
            return new ResponseEntity<>(accountService.getAccountDetails(userId), HttpStatus.OK);
        } else {
            logger.info("User not logged in!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedAccountDetails> getMyAccountDetailsAfterLogin(HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        long loggedInAccountId = accountService.findByEmail(user.getUsername()).getId();
        httpSession.setAttribute(SESSION_USER_ID_KEY, loggedInAccountId);
        return new ResponseEntity<>(new AuthenticatedAccountDetails(user), HttpStatus.OK);
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
