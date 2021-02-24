package hu.progmasters.fundraiser.exception;

public class TransferNotFoundException extends CustomRuntimeException {

    public TransferNotFoundException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "transfer.not.found";
    }
}
