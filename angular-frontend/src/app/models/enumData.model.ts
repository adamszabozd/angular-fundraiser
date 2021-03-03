import {CategoryOptionModel} from "./categoryOption.model";
import {StatusOptionModel} from "./statusOption.model";

export interface EnumDataModel {
    categoryOptions: Array<CategoryOptionModel>;
    statusOptions: Array<StatusOptionModel>;
}
