import {ChartDataElementModel} from "./chartDataElement.model";

export interface LineChartDataElementModel {
    name: string;
    series: Array<ChartDataElementModel>;
}
