export interface MyTransferListPendingItemModel {
    id: number;
    senderAccountEmail: string;
    targetFundTitle: string;
    senderAmount: number;
    senderCurrency: string;
    targetAmount: number;
    targetCurrency: string;
}
