package hu.progmasters.fundraiser.domain;

public enum Status {
    ACTIVE("status.active"),
    PASSIVE("status.passive"),
    SUSPENDED("status.suspended");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
