package hu.progmasters.fundraiser.dto.fund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.time.LocalDate;

public class ModifyFundFormCommand {

    private Long id;

    private String modifiedShortDescription;

    private String modifiedLongDescription;

    @JsonIgnoreProperties
    private CommonsMultipartFile modifiedImageFile;

    private String oldImageUrl;

    private Double modifiedTargetAmount;

    private LocalDate modifiedEndDate;

    private String modifiedStatus;


    public static ModifyFundFormCommand getDummyModifyCommand(Long id){
        ModifyFundFormCommand instance = new ModifyFundFormCommand();
        instance.setId(id);
        instance.setModifiedShortDescription("Modified short description");
        instance.setModifiedLongDescription("Long Description");
        instance.setModifiedImageFile(null);
        instance.setOldImageUrl("https://cdn.iconscout.com/icon/free/png-256/k-characters-character-alphabet-letter-36028.png");
        instance.setModifiedTargetAmount(1000.0);
        instance.setModifiedEndDate(LocalDate.parse("2025-01-01"));
        instance.setModifiedStatus("ACTIVE");
        return instance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModifiedShortDescription() {
        return modifiedShortDescription;
    }

    public void setModifiedShortDescription(String modifiedShortDescription) {
        this.modifiedShortDescription = modifiedShortDescription;
    }

    public String getModifiedLongDescription() {
        return modifiedLongDescription;
    }

    public void setModifiedLongDescription(String modifiedLongDescription) {
        this.modifiedLongDescription = modifiedLongDescription;
    }

    public CommonsMultipartFile getModifiedImageFile() {
        return modifiedImageFile;
    }

    public void setModifiedImageFile(CommonsMultipartFile modifiedImageFile) {
        this.modifiedImageFile = modifiedImageFile;
    }

    public String getOldImageUrl() {
        return oldImageUrl;
    }

    public void setOldImageUrl(String oldImageUrl) {
        this.oldImageUrl = oldImageUrl;
    }

    public Double getModifiedTargetAmount() {
        return modifiedTargetAmount;
    }

    public void setModifiedTargetAmount(Double modifiedTargetAmount) {
        this.modifiedTargetAmount = modifiedTargetAmount;
    }

    public LocalDate getModifiedEndDate() {
        return modifiedEndDate;
    }

    public void setModifiedEndDate(LocalDate modifiedEndDate) {
        this.modifiedEndDate = modifiedEndDate;
    }

    public String getModifiedStatus() {
        return modifiedStatus;
    }

    public void setModifiedStatus(String modifiedStatus) {
        this.modifiedStatus = modifiedStatus;
    }
}
