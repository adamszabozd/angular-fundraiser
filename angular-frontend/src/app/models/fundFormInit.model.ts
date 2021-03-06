import {CurrencyOptionModel} from './currencyOption.model';
import {CategoryOptionModel} from './categoryOption.model';
import {StatusOptionModel} from "./statusOption.model";

export interface FundFormInitModel {
    currencyOptions: Array<CurrencyOptionModel>;
    categoryOptions: Array<CategoryOptionModel>;
    statusOptions: Array<StatusOptionModel>;

}
