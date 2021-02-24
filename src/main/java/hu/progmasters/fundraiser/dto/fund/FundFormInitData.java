package hu.progmasters.fundraiser.dto.fund;

import hu.progmasters.fundraiser.dto.exchange.CurrencyOption;

import java.util.ArrayList;
import java.util.List;

public class FundFormInitData {

    private List<CategoryOption> categoryOptions;
    private List<CurrencyOption> currencyOptions;
    private List<StatusOption> statusOptions;


    public FundFormInitData() {
        this.currencyOptions = new ArrayList<>();
        this.categoryOptions = new ArrayList<>();
        this.statusOptions = new ArrayList<>();
    }

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public List<CurrencyOption> getCurrencyOptions() {
        return currencyOptions;
    }

    public void setCategoryOptions(List<CategoryOption> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    public void setCurrencyOptions(List<CurrencyOption> currencyOptions) {
        this.currencyOptions = currencyOptions;
    }

    public List<StatusOption> getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(List<StatusOption> statusOptions) {
        this.statusOptions = statusOptions;
    }
}
