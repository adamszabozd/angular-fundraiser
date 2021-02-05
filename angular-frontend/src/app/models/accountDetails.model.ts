import {MyTransferListItemModel} from './myTransferListItem.model';
import {MyTransferListPendingItemModel} from "./myTransferListPendingItem.model";

export interface AccountDetailsModel {
    id: number;
    email: string;
    balance: number;
    accountRoleList: Array<string>;
    confirmedTransfers: Array<MyTransferListItemModel>;
    pendingTransfers: Array<MyTransferListPendingItemModel>;
}
