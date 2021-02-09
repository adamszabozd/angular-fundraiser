package hu.progmasters.fundraiser.domain;

public enum FundCategory {
    MEDICAL("Medical"),
    EDUCATION("Education"),
    EMERGENCY("Emergency"),
    NONPROFIT("Non-profit");

    private String displayName;

    FundCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
