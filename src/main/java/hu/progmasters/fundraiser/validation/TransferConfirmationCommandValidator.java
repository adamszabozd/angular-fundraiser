package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.dto.TransferConfirmationCommand;
import hu.progmasters.fundraiser.service.SharedValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;

@Component
public class TransferConfirmationCommandValidator implements Validator {

    private final SharedValidationService validationService;
    private final HttpServletRequest request;

    @Autowired
    public TransferConfirmationCommandValidator(SharedValidationService validationService, HttpServletRequest request) {
        this.validationService = validationService;
        this.request = request;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TransferConfirmationCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferConfirmationCommand transferConfirmationCommand = (TransferConfirmationCommand) target;
        String code = transferConfirmationCommand.getConfirmationCode();
        if (!validationService.pendingTransferExistsAndSourceIsRight(code, request.getRemoteAddr())) {
            errors.rejectValue("confirmationCode", "pendingTransfer.invalidConfirmationCode");
        }
    }

}
