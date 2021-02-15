package hu.progmasters.fundraiser.dto.fund;



import java.time.LocalDate;

public class FundFormCommand {

    private String title;

    private String shortDescription;

    private String longDescription;

    private String imageUrl;

    private String category;

    private Double targetAmount;

    private LocalDate endDate;


    public static FundFormCommand getDummyInstance(String name) {
        FundFormCommand instance = new FundFormCommand();
        instance.setTitle(name);
        instance.setShortDescription("Short description two");
        instance.setLongDescription("Long Descripton tow");
        instance.setImageUrl("image_two.jpg");
        instance.setCategory("MEDICAL");
        instance.setTargetAmount(1001.0);
        instance.setEndDate(LocalDate.now());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public LocalDate getEndDate() {
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
