package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FundFormCommand {

    private String title;

    private String shortDescription;

    private String longDescription;

    @JsonIgnoreProperties
    private CommonsMultipartFile imageFile;

    private String category;

    private Double targetAmount;

    private String currency;

    private String endDate;

    private String status;

    public static FundFormCommand getDummyInstance(String title) {
        FundFormCommand instance = new FundFormCommand();
        instance.setTitle(title);
        instance.setShortDescription("Short description");
        instance.setLongDescription("Long Description");
        instance.setImageFile(null);
        instance.setCategory("MEDICAL");
        instance.setTargetAmount(1000.0);
        instance.setCurrency("EUR");
        instance.setEndDate("2025-01-01");
        instance.setStatus("ACTIVE");
        return instance;
    }

    public static FundFormCommand getDummyPassiveFund() {
        FundFormCommand instance = new FundFormCommand();
        instance.setTitle("Passive Fund");
        instance.setShortDescription("Short description");
        instance.setLongDescription("Long Description");
        instance.setImageFile(null);
        instance.setCategory("MEDICAL");
        instance.setTargetAmount(1000.0);
        instance.setCurrency("EUR");
        instance.setEndDate("2025-01-01");
        instance.setStatus("PASSIVE");
        return instance;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getCategory() {
        return category;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CommonsMultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(CommonsMultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
