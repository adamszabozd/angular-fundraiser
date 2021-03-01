package hu.progmasters.fundraiser.exception;

public class TransferNotFoundOrNotOwnException extends CustomRuntimeException {

    public TransferNotFoundOrNotOwnException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "transfer.not.found";
    }
}
