package hu.progmasters.fundraiser.dto.transfer.create;

public class TransferConfirmationCommand {

    private String confirmationCode;

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
