package hu.progmasters.fundraiser.exception;

public class AlreadyConfirmedTransferException extends RuntimeException {

    private final String accountEmail;

    public AlreadyConfirmedTransferException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getCode() {
        return "already.confirmed.transfer.confirmation";
    }

}
