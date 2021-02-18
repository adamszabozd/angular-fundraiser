import {CurrencyOptionModel} from './currencyOption.model';
import {CategoryOptionModel} from './categoryOption.model';

export interface FundFormInitModel {
    currencyOptions: Array<CurrencyOptionModel>;
    categoryOptions: Array<CategoryOptionModel>;

}
