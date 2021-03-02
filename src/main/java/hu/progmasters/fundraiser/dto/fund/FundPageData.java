package hu.progmasters.fundraiser.dto.fund;

import java.util.List;

public class FundPageData {

    private Long count;
    private List<FundListItem> funds;

    public FundPageData(Long count, List<FundListItem> funds) {
        this.count = count;
        this.funds = funds;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<FundListItem> getFunds() {
        return funds;
    }

    public void setFunds(List<FundListItem> funds) {
        this.funds = funds;
    }
}
