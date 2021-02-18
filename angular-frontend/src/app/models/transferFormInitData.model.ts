import {TargetFundOptionModel} from './targetFundOptionModel';
import {CurrencyOptionModel} from './currencyOption.model';

export interface TransferFormInitDataModel {
    targetFundOptions: Array<TargetFundOptionModel>;
    balance: number;
    currency: string;
    currencyOptions: Array<CurrencyOptionModel>;
}
