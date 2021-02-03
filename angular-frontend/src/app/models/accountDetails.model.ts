import {MyTransferListItemModel} from './myTransferListItem.model';

export interface AccountDetailsModel {
    id: number;
    email: string;
    balance: number;
    accountRoleList: Array<string>;
    outgoingTransfers: Array<MyTransferListItemModel>;
}
