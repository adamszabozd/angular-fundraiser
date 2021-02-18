package hu.progmasters.fundraiser.exception;

//TODO - Review: Bátran vegyetek fel újabb packegeket, hogy csoportosítsátok vele az összetartozó osztályokat!!
public class ConfirmedTransferDeleteException extends RuntimeException {

    private final String accountEmail;

    public ConfirmedTransferDeleteException(String message, String accountEmail) {
        super(message);
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

}
