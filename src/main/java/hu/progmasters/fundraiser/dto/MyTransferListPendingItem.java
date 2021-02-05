package hu.progmasters.fundraiser.dto;

import hu.progmasters.fundraiser.domain.Transfer;

import java.time.format.DateTimeFormatter;

public class MyTransferListPendingItem {

    private Long id;

    private String senderAccountEmail;

    private String targetFundTitle;

    private Integer amount;

    public MyTransferListPendingItem(Transfer transfer) {
        this.id = transfer.getId();
        this.senderAccountEmail = transfer.getSource().getEmail();
        this.targetFundTitle = transfer.getTarget().getFundTitle();
        this.amount = transfer.getAmount();
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

    public Integer getAmount() {
        return amount;
    }

}
