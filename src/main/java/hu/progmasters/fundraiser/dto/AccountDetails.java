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

package hu.progmasters.fundraiser.dto;

import hu.progmasters.fundraiser.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDetails implements Comparable<AccountDetails> {

    private Long id;
    private String username;
    private String email;
    private String goal;
    private Integer balance;
    private Integer funds;
    private List<TransferListItem> incomingTransfers = new ArrayList<>();
    private List<TransferListItem> outgoingTransfers = new ArrayList<>();

    public AccountDetails(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.goal = account.getGoal();
        this.balance = account.getBalance();
        this.funds = account.getFunds();
    }

    public void setIncomingTransfers(List<TransferListItem> incomingTransfers) {
        this.incomingTransfers = incomingTransfers;
    }

    public void setOutgoingTransfers(List<TransferListItem> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGoal() {
        return goal;
    }

    public Integer getBalance() {
        return balance;
    }

    public Integer getFunds() {
        return funds;
    }

    public List<TransferListItem> getIncomingTransfers() {
        return incomingTransfers;
    }

    public List<TransferListItem> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDetails)) return false;
        AccountDetails that = (AccountDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(AccountDetails otherAccountDetails) {
        return otherAccountDetails.funds.compareTo(this.funds);
    }
}
