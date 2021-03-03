package hu.progmasters.fundraiser.dto.fund;

import java.time.LocalDate;

public class ModifyFundFormCommand {

    private Long id;

    private String shortDescription;

    private String longDescription;

    private Double targetAmount;

    private LocalDate endDate;

    private String status;


    public static ModifyFundFormCommand getDummyModifyCommand(Long id){
        ModifyFundFormCommand instance = new ModifyFundFormCommand();
        instance.setId(id);
        instance.setShortDescription("Modified short description");
        instance.setLongDescription("Long Description");
        instance.setTargetAmount(1000.0);
        instance.setEndDate(LocalDate.parse("2025-01-01"));
        instance.setStatus("ACTIVE");
        return instance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
