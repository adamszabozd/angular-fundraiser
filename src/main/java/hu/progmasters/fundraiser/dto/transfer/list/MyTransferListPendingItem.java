package hu.progmasters.fundraiser.dto.transfer.list;

import hu.progmasters.fundraiser.domain.Transfer;

public class MyTransferListPendingItem {

    private final Long id;

    private final String senderAccountEmail;

    private final String targetFundTitle;

    private final Double senderAmount;

    private final String senderCurrency;

    private final Double targetAmount;

    private final String targetCurrency;

    public MyTransferListPendingItem(Transfer transfer) {
        this.id = transfer.getId();
        this.senderAccountEmail = transfer.getSource().getEmail();
        this.targetFundTitle = transfer.getTarget().getFundTitle();
        this.senderAmount = transfer.getSenderAmount();
        this.senderCurrency = transfer.getSenderCurrency().name();
        this.targetAmount = transfer.getTargetAmount();
        this.targetCurrency = transfer.getTargetCurrency().name();
    }

    public Long getId() {
        return id;
    }

    public String getSenderAccountEmail() {
        return senderAccountEmail;
    }

    public String getTargetFundTitle() {
        return targetFundTitle;
    }

    public Double getSenderAmount() {
        return senderAmount;
    }

    public String getSenderCurrency() {
        return senderCurrency;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

}
