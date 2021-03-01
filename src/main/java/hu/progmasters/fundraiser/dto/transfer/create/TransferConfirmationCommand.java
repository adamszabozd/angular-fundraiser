package hu.progmasters.fundraiser.dto.transfer.create;

public class TransferConfirmationCommand {

    private Long id;
    private String confirmationCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
