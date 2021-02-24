package hu.progmasters.fundraiser.exception;

public class AlreadyConfirmedTransferException extends CustomRuntimeException {

    public AlreadyConfirmedTransferException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "already.confirmed.transfer.confirmation";
    }

}
