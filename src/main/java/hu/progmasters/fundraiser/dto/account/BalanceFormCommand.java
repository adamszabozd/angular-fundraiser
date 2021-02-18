package hu.progmasters.fundraiser.dto.account;

public class BalanceFormCommand {
    private Long id;
    private Double addAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(Double addAmount) {
        this.addAmount = addAmount;
    }

}
