package hu.progmasters.fundraiser.validation;

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
        if (fund.getTitle().length() < 5 || fund.getTitle().length() > 100) {
            errors.rejectValue("title", "title.length.wrong");
        }
        if (validationService.fundTitleAlreadyExist(fund.getTitle())) {
            errors.rejectValue("title", "title.already.exist");
        }
        if (fund.getShortDescription() == null || fund.getShortDescription().isEmpty()) {
            errors.rejectValue("shortDescription", "short.description.missing");
        }
        if (fund.getShortDescription().length() < 5 || fund.getShortDescription().length() > 200) {
            errors.rejectValue("shortDescription", "short.description.length.wrong");
        }
        if (fund.getCategory() == null) {
            errors.rejectValue("category", "category.missing");
        }
        if (fund.getTargetAmount() == null) {
            errors.rejectValue("targetAmount", "target.amount.missing");
        } else if (fund.getTargetAmount() <= 0) {
            errors.rejectValue("targetAmount", "target.amount.wrong");
        }

        if (fund.getEndDate() != null && fund.getEndDate().isBefore(LocalDate.now())) {
            errors.rejectValue("endDate", "end.date.wrong");
        }
        if (fund.getImageUrl() != null && fund.getImageUrl().length() > 255) {
            errors.rejectValue("imageUrl", "url.too.long");
        }

    }
}
