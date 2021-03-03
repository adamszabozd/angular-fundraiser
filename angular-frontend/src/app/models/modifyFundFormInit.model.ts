import {StatusOptionModel} from "./statusOption.model";
import {CategoryOptionModel} from "./categoryOption.model";

export interface ModifyFundFormInitModel {

    title: string;
    shortDescription: string;
    longDescription: string;
    category: string;
    targetAmount: number;
    currency: string;
    endDate: Date;
    status: string;
    statusOptions: Array<StatusOptionModel>;
    categoryOptions: Array<CategoryOptionModel>;
}
