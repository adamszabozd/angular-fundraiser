package hu.progmasters.fundraiser.domain;

public enum FundCategory {
    MEDICAL("category.medical"),
    EDUCATION("category.education"),
    EMERGENCY("category.emergency"),
    NONPROFIT("category.nonprofit");

    private final String code;

    FundCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
