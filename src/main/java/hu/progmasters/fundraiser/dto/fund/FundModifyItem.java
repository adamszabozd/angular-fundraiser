package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.fundraiser.domain.Fund;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FundModifyItem {

    private final String title;

    private final String shortDescription;

    private final String longDescription;

    private final String imageUrl;

    private final String category;

    private final Double targetAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final String status;

    private List<StatusOption> statusOptions;

    public FundModifyItem(Fund fund, List<StatusOption> statusOptions) {
        this.title = fund.getFundTitle();
        this.shortDescription = fund.getShortDescription();
        this.longDescription = fund.getLongDescription();
        this.imageUrl = fund.getImageUrl();
        this.category = fund.getFundCategory().name();
        this.targetAmount = fund.getTargetAmount();
        this.endDate = fund.getEndDate();
        this.status = fund.getStatus().name();
        this.statusOptions = statusOptions;
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

    public String getStatus() {
        return status;
    }

    public List<StatusOption> getStatusOptions() {
        return statusOptions;
    }
}
