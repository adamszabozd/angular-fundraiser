package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;

public class FundListItem {

    private final Long id;

    private final String imageUrl;

    private final String title;

    private final String shortDescription;

    private final Double targetAmount;

    private final String currency;

    private final Double raisedAmount;

    private final Long progress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final String creatorName;

    private final String category;

    private final String status;

    public FundListItem(Fund fund, String categoryDisplayName) {
        this.id = fund.getId();
        this.imageUrl = fund.getImageUrl();
        this.title = fund.getFundTitle();
        this.shortDescription = fund.getShortDescription();
        this.targetAmount = fund.getTargetAmount();
        this.currency = fund.getCurrency().name();
        this.raisedAmount = fund.getRaisedAmount();
        this.progress = calculateProgress(raisedAmount, targetAmount);
        this.endDate = fund.getEndDate();
        this.creatorName = fund.getCreator().getUsername();
        this.category = categoryDisplayName;
        this.status = fund.getStatus().name();
    }

    private Long calculateProgress(Double raisedAmount, Double targetAmount) {
        long result = Math.round((raisedAmount / targetAmount) * 100);
        if (result > 100) {
            result = (long) 100;
        }
        return result;
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

    public String getCurrency() {
        return currency;
    }

    public Long getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }
}
