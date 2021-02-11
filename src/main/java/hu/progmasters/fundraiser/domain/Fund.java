package hu.progmasters.fundraiser.domain;

import hu.progmasters.fundraiser.dto.fund.FundFormCommand;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fund")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fund_title")
    private String fundTitle;

    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private FundCategory fundCategory;

    @Column(name = "amount")
    private Integer raisedAmount;

    @Column(name = "target_amount")
    private Integer targetAmount;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "creator")
    private Account creator;

    public Fund() {
    }

    public Fund(FundFormCommand fundFormCommand, Account account) {
        this.fundTitle = fundFormCommand.getTitle();
        this.shortDescription = fundFormCommand.getShortDescription();
        this.longDescription = fundFormCommand.getLongDescription();
        this.imageUrl = fundFormCommand.getImageUrl();
        this.fundCategory = FundCategory.valueOf(fundFormCommand.getCategory());
        this.raisedAmount = 0;
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

    public Integer getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(Integer amount) {
        this.raisedAmount = amount;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
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
}
