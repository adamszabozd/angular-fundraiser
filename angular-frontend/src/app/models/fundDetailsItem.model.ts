import {DailyDonationModel} from "./dailyDonation.model";

export interface FundDetailsItemModel{
    id: number;
    imageUrl: string;
    title: string;
    longDescription: string;
    targetAmount: number;
    currency: number;
    raisedAmount: number;
    endDate: Date;
    creatorName: string;
    category: string;
    numberOfBackers: number;
    dailyDonations: Array<DailyDonationModel>
}
