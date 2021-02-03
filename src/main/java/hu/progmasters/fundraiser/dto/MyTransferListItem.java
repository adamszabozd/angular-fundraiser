package hu.progmasters.fundraiser.dto;

import hu.progmasters.fundraiser.domain.Transfer;

import java.time.format.DateTimeFormatter;

public class MyTransferListItem {

    private Long id;

    private String senderAccountEmail;

    private String targetFundTitle;

    private Integer amount;

    private String timeStamp;

    public MyTransferListItem(Transfer transfer) {
        this.id = transfer.getId();
        this.senderAccountEmail = transfer.getSource().getEmail();
        this.targetFundTitle = transfer.getTarget().getFundTitle();
        this.amount = transfer.getAmount();
        this.timeStamp = transfer.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    public String getTimeStamp() {
        return timeStamp;
    }

}
