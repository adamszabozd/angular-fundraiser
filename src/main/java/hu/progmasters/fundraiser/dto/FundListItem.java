package hu.progmasters.fundraiser.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;

public class FundListItem {

    private String imageUrl;

    private String title;

    private String shortDescription;

    private Integer targetAmount;

    private Integer aimAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String creatorName;

    public FundListItem(Fund fund) {
        this.imageUrl = fund.getImageUrl();
        this.title = fund.getFundTitle();
        this.shortDescription = fund.getShortDescription();
        this.targetAmount = fund.getTargetAmount();
        this.aimAmount = fund.getRaisedAmount();
        this.endDate = fund.getEndDate();
        this.creatorName = fund.getCreator().getUsername();
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

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public Integer getAimAmount() {
        return aimAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
