package hu.progmasters.fundraiser.dto.transfer.create;

import hu.progmasters.fundraiser.domain.Fund;

public class TargetFundOption {

    private long id;
    private String title;

    public TargetFundOption(Fund fund) {
        this.id = fund.getId();
        this.title = fund.getFundTitle();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
