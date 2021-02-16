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

import hu.progmasters.fundraiser.dto.account.AccountRegistrationCommand;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable = false, updatable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Size(min=4, max=20)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Min(0)
    @Column(name = "balance", nullable = false)
    private Double balance = 5000.0;

    @Column(name = "account_currency",
            nullable = false)
    private Currency currency = Currency.EUR;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = AccountRole.class,
                       fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles")
    private List<AccountRole> accountRoleList = new ArrayList<>();

    @OneToMany(mappedBy = "source")
    private List<Transfer> outgoingTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Fund> funds = new ArrayList<>();

    public Account() {
    }

    public Account(AccountRegistrationCommand registrationCommand, String hashedPassword) {
        this.email = registrationCommand.getEmail();
        this.password = hashedPassword;
        this.username = registrationCommand.getUsername();
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
