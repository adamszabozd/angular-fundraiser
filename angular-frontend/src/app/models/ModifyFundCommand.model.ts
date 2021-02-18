export interface ModifyFundCommandModel{

    id: number;
    shortDescription: string;
    longDescription: string;
    imageUrl: string;
    targetAmount: number;
    currency: string;
    endDate: Date;

}
