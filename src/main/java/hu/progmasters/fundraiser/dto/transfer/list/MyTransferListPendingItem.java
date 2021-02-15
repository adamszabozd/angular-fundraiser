package hu.progmasters.fundraiser.dto.transfer.list;

import hu.progmasters.fundraiser.domain.Transfer;

import java.time.format.DateTimeFormatter;

public class MyTransferListPendingItem {

    private Long id;

    private String senderAccountEmail;

    private String targetFundTitle;

    private Double amount;

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

    public Double getAmount() {
        return amount;
    }

}
