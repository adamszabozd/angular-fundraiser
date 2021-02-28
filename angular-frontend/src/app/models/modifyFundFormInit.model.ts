import {StatusOptionModel} from "./statusOption.model";

export interface ModifyFundFormInitModel {

    title: string;
    shortDescription: string;
    longDescription: string;
    imageUrl: string;
    category: string;
    targetAmount: number;
    endDate: Date;
    status: string;
    statusOptions: Array<StatusOptionModel>;
}
