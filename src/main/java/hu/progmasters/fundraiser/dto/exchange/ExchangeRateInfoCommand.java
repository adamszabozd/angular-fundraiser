package hu.progmasters.fundraiser.dto.exchange;

import java.util.List;

public class ExchangeRateInfoCommand {

    private final String accountCurrency;
    private final List<CurrencyOption> currencyOptions;

    public ExchangeRateInfoCommand(String accountCurrency, List<CurrencyOption> currencyOptions) {
        this.accountCurrency = accountCurrency;
        this.currencyOptions = currencyOptions;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public List<CurrencyOption> getCurrencyOptions() {
        return currencyOptions;
    }

}
