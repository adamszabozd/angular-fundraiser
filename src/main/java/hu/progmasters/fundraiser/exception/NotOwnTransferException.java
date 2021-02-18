package hu.progmasters.fundraiser.exception;

public class NotOwnTransferException extends RuntimeException {

    private final String accountEmail;

    public NotOwnTransferException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getCode() {
        return "not.own.transfer.error";
    }

}
