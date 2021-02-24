package hu.progmasters.fundraiser.exception;

//TODO - Review: Bátran vegyetek fel újabb packegeket, hogy csoportosítsátok vele az összetartozó osztályokat!!
public class ConfirmedTransferDeletionException extends CustomRuntimeException {

    public ConfirmedTransferDeletionException(String accountEmail) {
        super(accountEmail);
    }

    @Override
    public String getCode() {
        return "confirmed.transfer.deletion.error";
    }

}
