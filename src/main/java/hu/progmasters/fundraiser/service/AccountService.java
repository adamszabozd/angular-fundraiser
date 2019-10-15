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
import hu.progmasters.fundraiser.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccountService {


    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void create(Account account, HttpServletRequest request) {
        account.setIpAddress(request.getRemoteAddr());
        accountRepository.save(account);
    }

    public Account findByIpAddress(String ipAddress) {
        return accountRepository.findByIpAddress(ipAddress);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findMyAccount(String ipAddress) {
        return findByIpAddress(ipAddress);
    }

    public List<AccountDetails> getAllAccountDetailsExceptOwn(Account myAccount) {
        List<AccountDetails> accountDetails = new ArrayList<>();
        List<Account> accounts = accountRepository.findAll();
        accounts.remove(myAccount);
        for (Account account : accounts) {
            accountDetails.add(new AccountDetails(account));
        }
        return accountDetails;
    }

}
