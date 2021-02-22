package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;

public class FundDetailsItem {

    private Long id;

    private String imageUrl;

    private String title;

    private String shortDescription;

    private String longDescription;

    private Double targetAmount;

    private String currency;

    private Double raisedAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String creatorName;

    private String category;

    private Long numberOfBackers;

    public FundDetailsItem(Fund fund, Long numberOfBackers, String categoryDisplayName) {
        this.id = fund.getId();
        this.imageUrl = fund.getImageUrl();
        this.title = fund.getFundTitle();
        this.shortDescription = fund.getShortDescription();
        this.longDescription = fund.getLongDescription();
        this.targetAmount = fund.getTargetAmount();
        this.currency = fund.getCurrency().name();
        this.raisedAmount = fund.getRaisedAmount();
        this.endDate = fund.getEndDate();
        this.creatorName = fund.getCreator().getUsername();
        this.category = categoryDisplayName;
        this.numberOfBackers = numberOfBackers;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public Double getTargetAmount() {
        return targetAmount;
    }

    public Double getRaisedAmount() {
        return raisedAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCategory() {
        return category;
    }

    public Long getNumberOfBackers() {
        return numberOfBackers;
    }

    public String getCurrency() {
        return currency;
    }

}
