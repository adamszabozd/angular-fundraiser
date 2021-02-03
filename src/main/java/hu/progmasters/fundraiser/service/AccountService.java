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
import hu.progmasters.fundraiser.dto.AccountDetails;
import hu.progmasters.fundraiser.dto.AccountRegistrationCommand;
import hu.progmasters.fundraiser.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {


    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public long create(AccountRegistrationCommand command) {
        String encryptedPassword = passwordEncoder.encode(command.getPassword());
        Account savedAccount = accountRepository.save(new Account(command, encryptedPassword));
        return savedAccount.getId();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByEmail(String email) {
       return accountRepository.findByEmail(email);
    }

    public Account findById(Long userId) {
        return accountRepository
                .findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public AccountDetails getAccountDetails(Long userId) {
        return new AccountDetails(findById(userId));
    }

}
