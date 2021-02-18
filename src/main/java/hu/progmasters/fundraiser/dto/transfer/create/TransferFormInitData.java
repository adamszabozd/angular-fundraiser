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

package hu.progmasters.fundraiser.dto.transfer.create;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.dto.exchange.CurrencyOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransferFormInitData {

    private List<TargetFundOption> targetFundOptions;
    private List<CurrencyOption> currencyOptions;
    private double balance;
    private String currency;

    public TransferFormInitData(List<Fund> fundList, Account account) {
        this.targetFundOptions = fundList.stream().map(TargetFundOption::new).collect(Collectors.toList());
        this.currencyOptions = new ArrayList<>();
        this.balance = account.getBalance();
        this.currency = account.getCurrency().name();
    }

    public void setCurrencyOptions(List<CurrencyOption> currencyOptions) {
        this.currencyOptions = currencyOptions;
    }

    public List<TargetFundOption> getTargetFundOptions() {
        return targetFundOptions;
    }

    public List<CurrencyOption> getCurrencyOptions() {
        return currencyOptions;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

}
