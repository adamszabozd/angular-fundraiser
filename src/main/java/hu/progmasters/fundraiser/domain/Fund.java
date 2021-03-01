package hu.progmasters.fundraiser.domain;

import hu.progmasters.fundraiser.dto.fund.FundFormCommand;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fund")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Size(min = 5, max = 100)
    @Column(name = "fund_title", unique = true, nullable = false)
    private String fundTitle;

    @Size(min = 5, max = 250)
    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "long_description",
            columnDefinition = "TEXT")
    private String longDescription;

    @Size(min = 5, max = 1000)
    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private FundCategory fundCategory;

    @Min(0)
    @Column(name = "amount", nullable = false)
    private Double raisedAmount;

    @Min(1)
    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "fund_currency",
            nullable = false)
    private Currency currency = Currency.EUR;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Future
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private Account creator;

    @OneToMany(mappedBy = "target")
    List<Transfer> transferList;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    public Fund() {
    }

    public Fund(FundFormCommand fundFormCommand, Account account, String imageUrl) {
        this.fundTitle = fundFormCommand.getTitle();
        this.shortDescription = fundFormCommand.getShortDescription();
        PolicyFactory policy = Sanitizers.BLOCKS
                .and(Sanitizers.FORMATTING)
                .and(Sanitizers.LINKS)
                .and(Sanitizers.STYLES)
                .and(Sanitizers.TABLES);
        this.longDescription = policy.sanitize(fundFormCommand.getLongDescription());
        this.imageUrl = imageUrl;
        this.fundCategory = FundCategory.valueOf(fundFormCommand.getCategory());
        this.raisedAmount = 0.0;
        this.targetAmount = fundFormCommand.getTargetAmount();
        this.currency = Currency.valueOf(fundFormCommand.getCurrency());
        if (fundFormCommand.getEndDate() != null) {
            this.endDate = LocalDate.parse(fundFormCommand.getEndDate());
        } else {
            this.endDate = null;
        }
        this.creator = account;
        this.status = Status.valueOf(fundFormCommand.getStatus());
        this.timeStamp = LocalDateTime.now();
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

    public List<Transfer> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Transfer> transferList) {
        this.transferList = transferList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

}
