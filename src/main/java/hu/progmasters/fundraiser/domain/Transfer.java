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

import hu.progmasters.fundraiser.dto.transfer.create.TransferCreationCommand;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            insertable = false,
            updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transfer_from", nullable = false)
    private Account source;

    @ManyToOne
    @JoinColumn(name = "transfer_to", nullable = false)
    private Fund target;

    @Min(0)
    @Column(name = "sender_amount", nullable = false)
    private Double senderAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_currency")
    private Currency senderCurrency;

    @Min(0)
    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_currency")
    private Currency targetCurrency;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @Column(name = "confirmation_code", nullable = false)
    private String confirmationCode;

    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

    public Transfer() {
    }

    public Transfer(TransferCreationCommand transferCreationCommand, Account source, Fund target) {
        this.source = source;
        this.target = target;
        this.senderAmount = transferCreationCommand.getSenderAmount();
        this.senderCurrency = source.getCurrency();
        this.targetCurrency = target.getCurrency();
        this.timeStamp = LocalDateTime.now();
        this.confirmed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSource() {
        return source;
    }

    public void setSource(Account source) {
        this.source = source;
    }

    public Fund getTarget() {
        return target;
    }

    public void setTarget(Fund target) {
        this.target = target;
    }

    public Double getSenderAmount() {
        return senderAmount;
    }

    public void setSenderAmount(Double senderAmount) {
        this.senderAmount = senderAmount;
    }

    public Currency getSenderCurrency() {
        return senderCurrency;
    }

    public void setSenderCurrency(Currency senderCurrency) {
        this.senderCurrency = senderCurrency;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

}
