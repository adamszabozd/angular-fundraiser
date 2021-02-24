import {StatusOptionModel} from "./StatusOption.model";

export interface ModifyFormInitModel{

    title: string;
    shortDescription: string;
    longDescription: string;
    imageUrl: string;
    category: string;
    targetAmount: number;
    endDate: Date;
    status: String;
    statusOptions: Array<StatusOptionModel>;
}
