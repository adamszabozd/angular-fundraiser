package hu.progmasters.fundraiser.domain;

import javax.persistence.*;

@Entity
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundTitle;

    private String sortDescription;

    @Lob
    private String longDescription;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private FundCategory fundCategory;

    public Fund() {
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

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
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

}
