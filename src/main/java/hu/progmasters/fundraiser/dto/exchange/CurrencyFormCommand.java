package hu.progmasters.fundraiser.dto.exchange;

public class CurrencyFormCommand {

    private String newCurrency;
    private Double newBalance;

    public String getNewCurrency() {
        return newCurrency;
    }

    public void setNewCurrency(String newCurrency) {
        this.newCurrency = newCurrency;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

}
