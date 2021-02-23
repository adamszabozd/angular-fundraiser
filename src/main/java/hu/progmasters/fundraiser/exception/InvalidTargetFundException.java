package hu.progmasters.fundraiser.exception;

public class InvalidTargetFundException extends RuntimeException {

    private final String senderEmail;

    public InvalidTargetFundException(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getCode() {
        return "invalid.target.currency";
    }

}
