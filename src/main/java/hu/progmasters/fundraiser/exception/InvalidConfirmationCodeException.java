package hu.progmasters.fundraiser.exception;

public class InvalidConfirmationCodeException extends CustomRuntimeException {

    public InvalidConfirmationCodeException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "invalid.confirmation.code";
    }

}
