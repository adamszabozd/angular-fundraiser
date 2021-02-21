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

        if (fund.getShortDescription() == null || fund.getShortDescription().isEmpty()) {
            errors.rejectValue("shortDescription", "short.description.missing");
        }
        if (fund.getShortDescription().length() < 5 || fund.getShortDescription().length() > 250) {
            errors.rejectValue("shortDescription", "short.description.length.wrong");
        }
        if (fund.getTargetAmount() == null) {
            errors.rejectValue("targetAmount", "target.amount.missing");
        } else if (fund.getTargetAmount() <= 0) {
            errors.rejectValue("targetAmount", "target.amount.wrong");
        }

        if (fund.getEndDate() != null && fund.getEndDate().isBefore(LocalDate.now())) {
            errors.rejectValue("endDate", "end.date.wrong");
        }
        if (fund.getImageUrl() != null && fund.getImageUrl().length() > 1000) {
            errors.rejectValue("imageUrl", "url.too.long");
        }

    }
}
