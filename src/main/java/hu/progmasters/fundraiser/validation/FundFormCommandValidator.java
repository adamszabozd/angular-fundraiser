package hu.progmasters.fundraiser.validation;

import hu.progmasters.fundraiser.domain.Currency;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.domain.Status;
import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import hu.progmasters.fundraiser.service.SharedValidationService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class FundFormCommandValidator implements Validator {

    private final SharedValidationService validationService;

    public FundFormCommandValidator(SharedValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FundFormCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FundFormCommand fund = (FundFormCommand) target;
        if (fund.getTitle() == null || fund.getTitle().isEmpty()) {
            errors.rejectValue("title", "title.missing");
        }
        if (fund.getTitle().replaceAll("\\s", "").length() < 5 || fund.getTitle().length() > 100) {
            errors.rejectValue("title", "title.length.wrong");
        }
        if (validationService.fundTitleAlreadyExist(fund.getTitle())) {
            errors.rejectValue("title", "title.already.exist");
        }
        if (fund.getShortDescription() == null || fund.getShortDescription().isEmpty()) {
            errors.rejectValue("shortDescription", "short.description.missing");
        }
        if (fund.getShortDescription().replaceAll("\\s", "").length() < 5 || fund.getShortDescription().length() > 250) {
            errors.rejectValue("shortDescription", "short.description.length.wrong");
        }
        if (fund.getCategory() == null || fund.getCategory().length() == 0) {
            errors.rejectValue("category", "category.missing");
        } else {
            try {
                FundCategory.valueOf(fund.getCategory());
            } catch (IllegalArgumentException e) {
                errors.rejectValue("category", "category.invalid");
            }
        }
        if (fund.getTargetAmount() == null) {
            errors.rejectValue("targetAmount", "target.amount.missing");
        } else if (fund.getTargetAmount() <= 0) {
            errors.rejectValue("targetAmount", "target.amount.wrong");
        }
        if (fund.getCurrency() == null || fund.getCurrency().length() == 0) {
            errors.rejectValue("currency", "currency.missing");
        } else {
            try {
                Currency.valueOf(fund.getCurrency());
            } catch (IllegalArgumentException e) {
                errors.rejectValue("currency", "currency.invalid");
            }
        }
        if (fund.getEndDate() != null) {
            LocalDate date = LocalDate.parse(fund.getEndDate());
            if (date.isBefore(LocalDate.now())) {
                errors.rejectValue("endDate", "end.date.wrong");
            }
        }
        if (fund.getStatus() == null || fund.getStatus().length() == 0) {
            errors.rejectValue("status", "status.missing");
        } else {
            try {
                Status.valueOf(fund.getStatus());
            } catch (IllegalArgumentException e) {
                errors.rejectValue("status", "status.invalid");
            }
        }
    }

}

