export interface FundListItemModel{

    id: number;
    imageUrl: string;
    title: string;
    shortDescription: string;
    targetAmount: number;
    currency: string;
    raisedAmount: number;
    progress: number;
    endDate: Date;
    creatorName: string;
    category: string;
}
