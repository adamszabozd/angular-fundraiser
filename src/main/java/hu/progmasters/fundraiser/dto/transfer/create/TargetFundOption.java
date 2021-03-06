package hu.progmasters.fundraiser.dto.transfer.create;

import hu.progmasters.fundraiser.domain.Fund;

public class TargetFundOption {

    private final long id;
    private final String title;
    private final String targetCurrency;

    public TargetFundOption(Fund fund) {
        this.id = fund.getId();
        this.title = fund.getFundTitle();
        this.targetCurrency = fund.getCurrency().name();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

}
