package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;
import java.util.List;

public class FundDetailsItem {

    private final Long id;

    private final String imageUrl;

    private final String title;

    private final String shortDescription;

    private final String longDescription;

    private final Double targetAmount;

    private final String currency;

    private final Double raisedAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final String creatorName;

    private final String category;

    private final String status;

    private final Long numberOfBackers;

    private List<DailyDonation> lastWeekDonations;

    public FundDetailsItem(Fund fund, Long numberOfBackers, String categoryDisplayName, List<DailyDonation> lastWeekDonations) {
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
        this.lastWeekDonations = lastWeekDonations;
        this.status = fund.getStatus().name();
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

    public List<DailyDonation> getLastWeekDonations() {
        return lastWeekDonations;
    }

    public String getStatus() {
        return status;
    }
}
