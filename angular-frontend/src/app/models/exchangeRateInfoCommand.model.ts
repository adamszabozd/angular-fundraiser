import {CurrencyOptionModel} from './currencyOption.model';

export interface ExchangeRateInfoCommandModel {
    accountCurrency: string;
    currencyOptions: Array<CurrencyOptionModel>;
}
