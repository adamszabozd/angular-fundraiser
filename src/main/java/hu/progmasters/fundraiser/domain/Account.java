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

package hu.progmasters.fundraiser.domain;

import hu.progmasters.fundraiser.dto.AccountRegistrationCommand;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//TODO - REVIEW:
// - Tábla és oszlop neveket erősen ajánlott explicit kiírni!
// - Ha van bármi constraint, azt már most rakjátok ki ( valami nem lehet null, legyen-e default értéke stb ),
// nagyon sok fejfájástól meg tud óvni a későbbiekben
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private Integer balance;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = AccountRole.class,
                       fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles")
    private List<AccountRole> accountRoleList = new ArrayList<>();

    @OneToMany(mappedBy = "source")
    private List<Transfer> outgoingTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "target")
    private List<Transfer> incomingTransfers = new ArrayList<>();

    public Account() {
    }

    public Account(AccountRegistrationCommand registrationCommand, String hashedPassword) {
        this.email = registrationCommand.getEmail();
        this.password = hashedPassword;
        //TODO - REVIEW: MAGIC NUMBER !!! Menjen konstansba!! Vagy még szebb, ha esetleg application.yaml-ből van behúzva! :)
        this.balance = 5000;
        accountRoleList.add(AccountRole.ROLE_USER);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public List<AccountRole> getAccountRoleList() {
        return accountRoleList;
    }

    public void setAccountRoleList(List<AccountRole> accountRoleList) {
        this.accountRoleList = accountRoleList;
    }

    public List<Transfer> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    public void setOutgoingTransfers(List<Transfer> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    public List<Transfer> getIncomingTransfers() {
        return incomingTransfers;
    }

    public void setIncomingTransfers(List<Transfer> incomingTransfers) {
        this.incomingTransfers = incomingTransfers;
    }

}
