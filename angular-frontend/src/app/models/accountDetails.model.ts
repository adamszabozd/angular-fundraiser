import {MyTransferListItemModel} from './myTransferListItem.model';
import {MyTransferListPendingItemModel} from "./myTransferListPendingItem.model";
import {DonationPerFundModel} from "./donationPerFund.model";

export interface AccountDetailsModel {
    id: number;
    email: string;
    username: string;
    balance: number;
    currency: string;
    accountRoleList: Array<string>;
    confirmedTransfers: Array<MyTransferListItemModel>;
    pendingTransfers: Array<MyTransferListPendingItemModel>;
    donationsPerFund: Array<DonationPerFundModel>;
}
