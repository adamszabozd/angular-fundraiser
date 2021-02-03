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

import hu.progmasters.fundraiser.domain.Transfer;

import java.time.format.DateTimeFormatter;


// ezt max admin jogokkal rendelkező user fogja használni.
public class TransferListItem {

    private Long id;

    private String senderAccountEmail;

    private Long targetFundId;

    private Integer amount;

    private String timeStamp;

    public TransferListItem(Transfer transfer) {
        this.id = transfer.getId();
        this.senderAccountEmail = transfer.getSource().getEmail();
        this.targetFundId = transfer.getTarget().getId();
        this.amount = transfer.getAmount();
        this.timeStamp = transfer.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Long getId() {
        return id;
    }

    public String getSenderAccountEmail() {
        return senderAccountEmail;
    }

    public Long getTargetFundId() {
        return targetFundId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
