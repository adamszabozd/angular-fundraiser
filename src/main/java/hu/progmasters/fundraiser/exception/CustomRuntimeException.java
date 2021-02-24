package hu.progmasters.fundraiser.exception;

public abstract class CustomRuntimeException extends RuntimeException {

    private final String accountEmail;

    public CustomRuntimeException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public abstract String getCode();

}
