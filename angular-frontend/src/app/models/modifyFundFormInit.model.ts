import {StatusOptionModel} from "./statusOption.model";
import {CategoryOptionModel} from "./categoryOption.model";

export interface ModifyFundFormInitModel {

    id: number;
    title: string;
    shortDescription: string;
    longDescription: string;
    oldImageUrl: string;
    category: string;
    targetAmount: number;
    currency: string;
    endDate: Date;
    status: string;
    statusOptions: Array<StatusOptionModel>;
    categoryOptions: Array<CategoryOptionModel>;
}
