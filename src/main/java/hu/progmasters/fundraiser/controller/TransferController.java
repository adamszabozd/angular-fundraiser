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
import hu.progmasters.fundraiser.dto.transfer.create.TransferConfirmationCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;
import hu.progmasters.fundraiser.dto.transfer.create.TransferFormInitData;
import hu.progmasters.fundraiser.dto.transfer.list.MyTransferListPendingItem;
import hu.progmasters.fundraiser.service.*;
import hu.progmasters.fundraiser.validation.TransferConfirmationCommandValidator;
import hu.progmasters.fundraiser.validation.TransferCreationCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;
    private final AccountService accountService;
    private final FundService fundService;
    private final EmailSendingService emailSendingService;
    private final ExchangeService exchangeService;

    private final TransferCreationCommandValidator transferCreationCommandValidator;
    private final TransferConfirmationCommandValidator transferConfirmationCommandValidator;

    //TODO - Review: Ez elég sok függőségnek tűnik elsőre, biztos hogy nem lehetne ezeket valahová átrakni?
    @Autowired
    public TransferController(
            TransferService transferService,
            AccountService accountService,
            FundService fundService,
            ExchangeService exchangeService,
            EmailSendingService emailSendingService,
            TransferCreationCommandValidator transferCreationCommandValidator,
            TransferConfirmationCommandValidator transferConfirmationCommandValidator
    ) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.fundService = fundService;
        this.emailSendingService = emailSendingService;
        this.exchangeService = exchangeService;
        this.transferCreationCommandValidator = transferCreationCommandValidator;
        this.transferConfirmationCommandValidator = transferConfirmationCommandValidator;
    }

    @InitBinder("transferCreationCommand")
    protected void initCreationBinder(WebDataBinder binder) {
        binder.addValidators(transferCreationCommandValidator);
        //TODO - Review: Amennyiben így csináljátok, nem kell injektálni a validátort
        // Természetesen itt mérlegelni kell, hogy milyen sűrűn van használva, mi a nagyobb "pazarlás"
        // Mindig létrehozni, vagy folyamatosan tárolni a memóriában
        // binder.addValidators(new TransferConfirmationCommandValidator());
    }

    @InitBinder("transferConfirmationCommand")
    protected void initConfirmationBinder(WebDataBinder binder) {
        binder.addValidators(transferConfirmationCommandValidator);
    }

    @GetMapping("/newTransferData")
    public ResponseEntity<TransferFormInitData> newTransferData(Principal principal) {
        Account myAccount = accountService.findByEmail(principal.getName());
        List<Fund> targetFunds = fundService.findAll();
        TransferFormInitData initData = new TransferFormInitData(targetFunds, myAccount);
        initData.setCurrencyOptions(exchangeService.fetchRates());
        return new ResponseEntity<>(initData, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferFormInitData> concreteTransferData(@PathVariable Long id, Principal principal) {
        Account myAccount = accountService.findByEmail(principal.getName());
        List<Fund> targetFunds = fundService.findById(id);
        TransferFormInitData initData = new TransferFormInitData(targetFunds, myAccount);
        return new ResponseEntity<>(initData, HttpStatus.OK);
    }

        @PostMapping
    public ResponseEntity<Void> savePendingTransfer(@Valid @RequestBody TransferCreationCommand transferCreationCommand, Principal principal) {
        Transfer pendingTransfer = transferService.savePendingTransfer(transferCreationCommand, principal.getName());
        String confirmationCode = pendingTransfer.getUnencryptedConfirmationCode();
        String to = accountService.findByEmail(principal.getName()).getEmail();
        emailSendingService.sendConfirmationEmail(to, confirmationCode, pendingTransfer.getTarget().getFundTitle(), pendingTransfer.getAmount());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmTransfer(@Valid @RequestBody TransferConfirmationCommand transferConfirmationCommand, Principal principal) {
        transferService.confirmTransfer(transferConfirmationCommand, principal.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<MyTransferListPendingItem>> deletePendingTransfer(@PathVariable Long id, Principal principal) throws AccountNotFoundException {
        transferService.deleteTransfer(id, principal.getName());
        logger.info("Pending transfer deleted with id=" + id);
        return new ResponseEntity<>(transferService.getPendingTransfers(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/resend/{id}")
    public ResponseEntity<Void> resendConfirmationEmail(@PathVariable Long id, Principal principal) {
        //TODO - Review: Az összes ilyen logika menjen service rétegbe, semmit nem adunk vissza az eredményből,
        // mindent be lehetne húzni egy metódus alá valamelyik service-be
        Transfer pendingTransfer = transferService.getPendingTransferByIdAndEmail(id, principal.getName());
        transferService.generateNewConfirmationCode(pendingTransfer);
        String confirmationCode = pendingTransfer.getUnencryptedConfirmationCode();
        String to = principal.getName();
        emailSendingService.sendConfirmationEmail(to, confirmationCode, pendingTransfer.getTarget().getFundTitle(), pendingTransfer.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
