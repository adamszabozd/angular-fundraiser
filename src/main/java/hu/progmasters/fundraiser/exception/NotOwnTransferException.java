package hu.progmasters.fundraiser.exception;

public class NotOwnTransferException extends CustomRuntimeException {

    public NotOwnTransferException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "not.own.transfer.error";
    }

}
