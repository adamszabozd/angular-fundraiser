package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.dto.fund.ModifyFundFormCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class ModifyFundFormCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ModifyFundFormCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyFundFormCommand fund = (ModifyFundFormCommand) target;
        if (fund.getModifiedTargetAmount() == null) {
            errors.rejectValue("modifiedTargetAmount", "target.amount.missing");
        } else if (fund.getModifiedTargetAmount() <= 0) {
            errors.rejectValue("modifiedTargetAmount", "target.amount.wrong");
        }
        if (fund.getModifiedShortDescription() == null || fund.getModifiedShortDescription().isEmpty()) {
            errors.rejectValue("modifiedShortDescription", "short.description.missing");
        }
        if (fund.getModifiedShortDescription().length() < 5 || fund.getModifiedShortDescription().length() > 250) {
            errors.rejectValue("modifiedShortDescription", "short.description.length.wrong");
        }
        if (fund.getModifiedEndDate() != null) {
            LocalDate date = fund.getModifiedEndDate();
            if (date.isBefore(LocalDate.now())) {
                errors.rejectValue("modifiedEndDate", "end.date.wrong");
            }
        }
    }

}
