package hu.progmasters.fundraiser.dto.fund;

import java.util.List;

public class MyFundsData {

    private final List<FundListItem> fundListItems;

    private final List<CategoryOption> categoryOptions;

    public MyFundsData(List<FundListItem> fundListItems, List<CategoryOption> categoryOptions) {
        this.fundListItems = fundListItems;
        this.categoryOptions = categoryOptions;
    }

    public List<FundListItem> getFundListItems() {
        return fundListItems;
    }

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }
}
