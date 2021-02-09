package hu.progmasters.fundraiser.exception;

//TODO - Review: Bátran vegyetek fel újabb packegeket, hogy csoportosítsátok vele az összetartozó osztályokat.
// Pl a 2 új custom exception-öd is mehetne a saját package-ébe! :)
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
