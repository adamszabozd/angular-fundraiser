package hu.progmasters.fundraiser.dto.account;

public class DonationPerFund {
    private final String fundName;
    private final Double amount;

    public DonationPerFund(String fundName, Double amount) {
        this.fundName = fundName;
        this.amount = amount;
    }

    public String getFundName() {
        return fundName;
    }

    public Double getAmount() {
        return amount;
    }
}
