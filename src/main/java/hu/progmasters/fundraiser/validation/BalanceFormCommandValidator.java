package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.dto.account.BalanceFormCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BalanceFormCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BalanceFormCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BalanceFormCommand command = (BalanceFormCommand) target;
        if (command.getAddAmount() == null || command.getAddAmount() <= 0) {
            errors.rejectValue("addAmount", "addAmount.notPositive");
        }
    }

}
