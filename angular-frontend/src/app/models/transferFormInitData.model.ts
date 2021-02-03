import {TargetFundOptionModel} from './targetFundOptionModel';

export interface TransferFormInitDataModel {
    balance: number;
    targetFundOptions: Array<TargetFundOptionModel>;
}
