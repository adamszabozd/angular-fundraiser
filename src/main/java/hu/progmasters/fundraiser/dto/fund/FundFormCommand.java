package hu.progmasters.fundraiser.dto.fund;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FundFormCommand {

    private String title;

    private String shortDescription;

    private String longDescription;

    private CommonsMultipartFile imageFile;

    private String category;

    private String status;

    private Double targetAmount;

    private String currency;

    private String endDate;

    public static FundFormCommand getDummyInstance(String name) {
        FundFormCommand instance = new FundFormCommand();
        instance.setTitle(name);
        instance.setShortDescription("Short description two");
        instance.setLongDescription("Long Descripton tow");
        instance.setImageFile(null);
        instance.setCategory("MEDICAL");
        instance.setTargetAmount(1001.0);
        instance.setCategory("EUR");
        instance.setEndDate("2025-01-01");
        instance.setStatus("Active");
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
