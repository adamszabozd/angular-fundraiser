import {FundListItemModel} from "./fundListItem.model";
import {CategoryOptionModel} from "./categoryOption.model";

export interface FundPageDataModel {
    count: number;
    funds: FundListItemModel[];
    categoryOptions: CategoryOptionModel[];
}
