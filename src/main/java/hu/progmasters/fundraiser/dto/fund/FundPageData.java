package hu.progmasters.fundraiser.dto.fund;

import java.util.List;

public class FundPageData {

    private Long count;
    private List<FundListItem> funds;
    private List<CategoryOption> categoryOptions;

    public FundPageData(Long count, List<FundListItem> funds, List<CategoryOption> categoryOptions) {
        this.count = count;
        this.funds = funds;
        this.categoryOptions = categoryOptions;
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

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(List<CategoryOption> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }
}
