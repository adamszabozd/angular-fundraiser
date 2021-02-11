package hu.progmasters.fundraiser.dto;



import java.time.LocalDate;

public class FundFormCommand {

    private String title;

    private String shortDescription;

    private String longDescription;

    private String imageUrl;

    private String category;

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
