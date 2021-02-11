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

package hu.progmasters.fundraiser.dto.account;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Transfer;
import hu.progmasters.fundraiser.dto.transfer.list.MyTransferListItem;
import hu.progmasters.fundraiser.dto.transfer.list.MyTransferListPendingItem;

import java.util.List;
import java.util.stream.Collectors;

public class AccountDetails {

    private Long id;
    private String username;
    private String email;
    private Integer balance;
    private List<String> accountRoleList;
    private List<MyTransferListItem> confirmedTransfers;
    private List<MyTransferListPendingItem> pendingTransfers;

    public AccountDetails(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.balance = account.getBalance();
        this.accountRoleList = account.getAccountRoleList().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        this.confirmedTransfers = account.getOutgoingTransfers().stream()
                .filter(Transfer::getConfirmed).map(MyTransferListItem::new)
                .collect(Collectors.toList());
        this.pendingTransfers = account.getOutgoingTransfers().stream()
                .filter(t -> !t.getConfirmed())

                .map(MyTransferListPendingItem::new)
                .collect(Collectors.toList());
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

    public List<MyTransferListItem> getConfirmedTransfers() {
        return confirmedTransfers;
    }

    public List<String> getAccountRoleList() {
        return accountRoleList;
    }

    public List<MyTransferListPendingItem> getPendingTransfers() {
        return pendingTransfers;
    }

    public String getUsername() {
        return username;
    }
}
