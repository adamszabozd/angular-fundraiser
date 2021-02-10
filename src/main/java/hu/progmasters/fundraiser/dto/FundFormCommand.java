package hu.progmasters.fundraiser.dto;


import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class FundFormCommand {

    @Size(min = 5, max = 100, message = "The title can't be longer than {max} characters!")
    private String title;

    @Size(min = 5, max = 200, message = "Please give a description shorter than 200 characters!")
    private String shortDescription;

    private String longDescription;

    private String imageUrl;

    private String category;

    @Min(value = 1, message = "The target amount must be above zero!")
    private Integer targetAmount;

    private LocalDate endDate;

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
