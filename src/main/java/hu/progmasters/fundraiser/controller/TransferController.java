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
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.TransferConfirmationCommand;
import hu.progmasters.fundraiser.dto.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.TransferFormInitData;
import hu.progmasters.fundraiser.dto.TransferListItem;
import hu.progmasters.fundraiser.service.AccountService;
import hu.progmasters.fundraiser.service.EmailSendingService;
import hu.progmasters.fundraiser.service.FundService;
import hu.progmasters.fundraiser.service.TransferService;
import hu.progmasters.fundraiser.validation.TransferConfirmationCommandValidator;
import hu.progmasters.fundraiser.validation.TransferCreationCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;
    private final AccountService accountService;
    private final FundService fundService;

    private final EmailSendingService emailSendingService;
    private final TransferCreationCommandValidator transferCreationCommandValidator;
    private final TransferConfirmationCommandValidator transferConfirmationCommandValidator;

    @Autowired
    public TransferController(
            TransferService transferService,
            AccountService accountService,
            FundService fundService, EmailSendingService emailSendingService,
            TransferCreationCommandValidator transferCreationCommandValidator,
            TransferConfirmationCommandValidator transferConfirmationCommandValidator
    ) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.fundService = fundService;

        this.emailSendingService = emailSendingService;
        this.transferCreationCommandValidator = transferCreationCommandValidator;
        this.transferConfirmationCommandValidator = transferConfirmationCommandValidator;
    }

    @InitBinder("transferCreationCommand")
    protected void initCreationBinder(WebDataBinder binder) {
        binder.addValidators(transferCreationCommandValidator);
    }

    @InitBinder("transferConfirmationCommand")
    protected void initConfirmationBinder(WebDataBinder binder) {
        binder.addValidators(transferConfirmationCommandValidator);
    }

    @GetMapping("/newTransferData")
    public ResponseEntity<TransferFormInitData> newTransferData(Principal principal) {
        Account myAccount = accountService.findByEmail(principal.getName());
        List<Fund> targetFunds = fundService.findAll();
        TransferFormInitData initData = new TransferFormInitData(targetFunds, myAccount.getBalance());

        return new ResponseEntity<>(initData, HttpStatus.OK);
    }

    //TODO - REVIEW: Üzleti logikát ne rakjatok a controller rétegbe, ez mind mehet a servicebe,
    // ezért van itt ilyen sok függőség
    // Also, ha HTML-t szeretnél küldeni, érdemes esetleg a Thymelea-nek utánanézni, erre is van példakódom,
    // megosztom ha érdekel
    @PostMapping
    public ResponseEntity savePendingTransfer(@Valid @RequestBody TransferCreationCommand transferCreationCommand, Principal principal) {
        ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
        Transfer pendingTransfer = transferService.savePendingTransfer(transferCreationCommand, principal.getName());
        if (pendingTransfer == null) {
            logger.warn("Transfer failed, source or target account does not exist!");
            response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            String code = pendingTransfer.getConfirmationCode();
            String to = accountService.findByEmail(principal.getName()).getEmail();
            String body = "<h1 style=\"background-color:DodgerBlue;text-align:center;\">PROGmasters Fundraiser</h1>" +
                    "<p>A transfer was initiated from you account. Please click the following link for confirmation:</p>" +
                    "<p style=\"font-size:20px;font-weight:bold;\">http://localhost:4200/transfer-confirmation/" + code + "</p>";
            String topic = "Transfer confirmation";
            emailSendingService.sendHtmlEmail(to, body, topic);
        }
        return response;
    }

    //TODO - REVIEW: Ilyet is sokat látok, hogy controllerig visszaadtok entityt, és megnézitek hogy az null értékű-e
    // Ennél sokkal elegánsabb, ha a service rétegben hibánál exceptiont dobtok ( pl sajátot ),
    // majd azt az ExceptionHandler-ben lekezelitek!
    @PostMapping("/confirm")
    public ResponseEntity confirmTransfer(@Valid @RequestBody TransferConfirmationCommand transferConfirmationCommand) {
        ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
        Transfer transfer = transferService.confirmTransfer(transferConfirmationCommand);
        if (transfer == null) {
            logger.warn("Confirmation failed, no pending transfer exists with this confirmation code!");
            response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    // Ezt akkor fogjuk használni, ha csinálunk admin felületet. Amúgy nem listázzuk ki az átutalásokat nyilvánosan.
    @GetMapping
    public ResponseEntity<List<TransferListItem>> getAllTransferListItems() {
        List<TransferListItem> transferItems = transferService.findAll().stream()
                                                              .map(TransferListItem::new).collect(Collectors.toList());
        return new ResponseEntity<>(transferItems, HttpStatus.OK);
    }

}
