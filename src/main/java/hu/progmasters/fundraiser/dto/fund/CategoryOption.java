package hu.progmasters.fundraiser.dto.fund;

import hu.progmasters.fundraiser.domain.FundCategory;

public class CategoryOption {

    private String name;

    private String displayName;

    public CategoryOption(FundCategory fundCategory) {
        this.name = fundCategory.toString();
        this.displayName = fundCategory.getDisplayName();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
