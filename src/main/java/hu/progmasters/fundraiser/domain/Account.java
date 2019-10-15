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

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String ipAddress;

    private String goal;

    private Integer balance;

    private Integer funds;

    @OneToMany(mappedBy = "source")
    private List<Transfer> targetTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "target")
    private List<Transfer> sourceTransfers = new ArrayList<>();

    public Account() {
    }

    public Account(AccountRegistrationCommand accountRegistrationCommand) {
        this.username = accountRegistrationCommand.getUsername();
        this.goal = accountRegistrationCommand.getGoal();
        this.balance = 5000;
        this.funds = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getFunds() {
        return funds;
    }

    public void setFunds(Integer funds) {
        this.funds = funds;
    }

    public List<Transfer> getTargetTransfers() {
        return targetTransfers;
    }

    public void setTargetTransfers(List<Transfer> targetTransfers) {
        this.targetTransfers = targetTransfers;
    }

    public List<Transfer> getSourceTransfers() {
        return sourceTransfers;
    }

    public void setSourceTransfers(List<Transfer> sourceTransfers) {
        this.sourceTransfers = sourceTransfers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id != null ? id.equals(account.id) : account.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return goal + "(" + username + ")" + " - balance: " + balance + ", funds: " + funds;
    }
}
