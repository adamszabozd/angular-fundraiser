import {DailyDonationModel} from "./dailyDonation.model";
import {CategoryOptionModel} from './categoryOption.model';

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
    category: CategoryOptionModel;
    numberOfBackers: number;
    dailyDonations: Array<DailyDonationModel>
}
