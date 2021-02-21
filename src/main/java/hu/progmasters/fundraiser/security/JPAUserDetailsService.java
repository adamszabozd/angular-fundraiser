/*
 * Copyright © Progmasters (QTC Kft.), 2016-2019.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package hu.progmasters.fundraiser.security;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.AccountRole;
import hu.progmasters.fundraiser.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JPAUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public JPAUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if (optionalAccount.isPresent()) {
            String[] accountRoles = optionalAccount.get().getAccountRoleList().stream().map(AccountRole::name).toArray(String[]::new);
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(accountRoles);
            return User.withUsername(email).authorities(authorities).password(optionalAccount.get().getPassword()).build();
        }else {
            throw new UsernameNotFoundException("No account found with email: " + email);
        }
    }

}
