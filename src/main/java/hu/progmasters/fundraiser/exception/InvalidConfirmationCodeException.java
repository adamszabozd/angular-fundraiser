package hu.progmasters.fundraiser.exception;

public class InvalidConfirmationCodeException extends RuntimeException {

    private final String accountEmail;

    public InvalidConfirmationCodeException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getCode() {
        return "invalid.confirmation.code";
    }

}
