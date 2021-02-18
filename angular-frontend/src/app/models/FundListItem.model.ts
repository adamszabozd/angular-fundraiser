export interface FundListItemModel{

    id: number;
    imageUrl: string;
    title: string;
    shortDescription: string;
    longDescription: string;
    targetAmount: number;
    currency: string;
    raisedAmount: number;
    endDate: Date;
    creatorName: string;
    category: string;
}
