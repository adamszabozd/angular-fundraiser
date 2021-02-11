package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.dto.transfer.create.TransferConfirmationCommand;
import hu.progmasters.fundraiser.service.SharedValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TransferConfirmationCommandValidator implements Validator {

    private final SharedValidationService validationService;

    @Autowired
    public TransferConfirmationCommandValidator(SharedValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TransferConfirmationCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferConfirmationCommand transferConfirmationCommand = (TransferConfirmationCommand) target;
        String code = transferConfirmationCommand.getConfirmationCode();
        if (!validationService.pendingTransferExistsAndSourceIsRight(code)) {
            errors.rejectValue("confirmationCode", "pendingTransfer.invalidConfirmationCode");
        }
    }

}
