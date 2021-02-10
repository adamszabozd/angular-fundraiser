package hu.progmasters.fundraiser.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;

public class FundListItem {

    private Long id;

    private String imageUrl;

    private String title;

    private String shortDescription;

    private String longDescription;

    private Integer targetAmount;

    private Integer raisedAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String creatorName;

    public FundListItem(Fund fund) {
        this.id = fund.getId();
        this.imageUrl = fund.getImageUrl();
        this.title = fund.getFundTitle();
        this.shortDescription = fund.getShortDescription();
        this.longDescription = fund.getLongDescription();
        this.targetAmount = fund.getTargetAmount();
        this.raisedAmount = fund.getRaisedAmount();
        this.endDate = fund.getEndDate();
        this.creatorName = fund.getCreator().getUsername();
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

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public Integer getRaisedAmount() {
        return raisedAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
