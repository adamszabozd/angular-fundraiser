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

public class AccountDetails {

    private Long id;
    private String email;
    private Integer balance;
    private List<String> accountRoleList;
    private List<TransferListItem> incomingTransfers = new ArrayList<>();
    private List<TransferListItem> outgoingTransfers = new ArrayList<>();

    public AccountDetails(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.balance = account.getBalance();
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

    public String getEmail() {
        return email;
    }

    public Integer getBalance() {
        return balance;
    }

    public List<TransferListItem> getIncomingTransfers() {
        return incomingTransfers;
    }

    public List<TransferListItem> getOutgoingTransfers() {
        return outgoingTransfers;
    }

}
