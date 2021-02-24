package hu.progmasters.fundraiser.exception;

public class InvalidTargetFundException extends CustomRuntimeException {

    public InvalidTargetFundException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "invalid.target.currency";
    }

}
