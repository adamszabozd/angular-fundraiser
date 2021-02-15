package hu.progmasters.fundraiser.domain;

import hu.progmasters.fundraiser.dto.fund.FundFormCommand;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "fund")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            insertable = false,
            updatable = false)
    private Long id;

    @Size(min = 5,
          max = 100)
    @Column(name = "fund_title",
            unique = true,
            nullable = false)
    private String fundTitle;

    @Size(min = 5,
          max = 200)
    @Column(name = "short_description",
            nullable = false)
    private String shortDescription;

    @Column(name = "long_description",
            columnDefinition = "TEXT")
    private String longDescription;

    @Column(name = "image_url",
            nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category",
            nullable = false)
    private FundCategory fundCategory;

    @Min(0)
    @Column(name = "amount",
            nullable = false)
    private Double raisedAmount;

    @Min(1)
    @Column(name = "target_amount",
            nullable = false)
    private Double targetAmount;

    @Column(name = "fund_currency",
            nullable = false)
    private Currency currency = Currency.EUR;

    @Future
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "creator",
                nullable = false)
    private Account creator;

    public Fund() {
    }

    public Fund(FundFormCommand fundFormCommand, Account account) {
        this.fundTitle = fundFormCommand.getTitle();
        this.shortDescription = fundFormCommand.getShortDescription();
        this.longDescription = fundFormCommand.getLongDescription();
        this.imageUrl = fundFormCommand.getImageUrl();
        this.fundCategory = FundCategory.valueOf(fundFormCommand.getCategory());
        this.raisedAmount = 0.0;
        this.targetAmount = fundFormCommand.getTargetAmount();
        this.endDate = fundFormCommand.getEndDate();
        this.creator = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFundTitle() {
        return fundTitle;
    }

    public void setFundTitle(String fundTitle) {
        this.fundTitle = fundTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FundCategory getFundCategory() {
        return fundCategory;
    }

    public void setFundCategory(FundCategory fundCategory) {
        this.fundCategory = fundCategory;
    }

    public Double getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(Double amount) {
        this.raisedAmount = amount;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
