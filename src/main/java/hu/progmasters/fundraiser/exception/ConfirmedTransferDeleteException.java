package hu.progmasters.fundraiser.exception;

//TODO - Review: Bátran vegyetek fel újabb packegeket, hogy csoportosítsátok vele az összetartozó osztályokat!!
public class ConfirmedTransferDeleteException extends RuntimeException {

    private final String accountEmail;

    public ConfirmedTransferDeleteException(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getCode() {
        return "confirmed.transfer.deletion.error";
    }

}
