import {Component, OnInit} from '@angular/core';
import {FundsService} from '../../services/funds.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FundDetailsItemModel} from '../../models/fundDetailsItem.model';
import {ChartDataElementModel} from '../../models/chartDataElement.model';
import {LineChartDataElementModel} from '../../models/lineChartDataElement.model';
import {numberToString} from '../../utils/numberFormatter';

@Component({
               selector   : 'app-my-fund-details',
               templateUrl: './my-fund-details.component.html',
               styleUrls  : ['./my-fund-details.component.css'],
           })
export class MyFundDetailsComponent implements OnInit {

    id: number;
    title: string;
    fund: FundDetailsItemModel | undefined;
    barChartData: Array<ChartDataElementModel> | undefined;
    lineChartData: Array<LineChartDataElementModel> | undefined;
    lastWeekDonation: number = 0;

    // bar chart options
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showLegend = false;
    showXAxisLabel = false;
    xAxisLabel = 'Date';
    showYAxisLabel = false;
    yAxisLabel = 'Amount';
    colorScheme = {
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA'],
    };
    view: any[] = [400, 250];

    // additional options for line chart
    legend: boolean = false;
    showLabels: boolean = true;
    animations: boolean = true;
    xAxis: boolean = true;
    yAxis: boolean = true;
    timeline: boolean = false;

    numberToString = numberToString;

    constructor(private fundService: FundsService, private activatedRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe(
            (paraMap) => {
                this.id = Number.parseInt(paraMap.get("id"));
                this.fetchData();
            }
        )
        this.fundService.languageStatusUpdate.subscribe(() => {
            this.fetchData();
        })
    }

    fetchData() {
        this.fundService.fetchMyFundDetails(this.id).subscribe(
            (data) => {
                this.fund = data;
                for (let dailyDonation of this.fund.dailyDonations) {
                    this.lastWeekDonation += dailyDonation.amount;
                }
                this.barChartData = data.dailyDonations.slice(Math.max(0, data.dailyDonations.length - 7)).map(x => {
                    return {name: x.date, value: x.amount};
                });
                let sum = 0;
                let cumulativeDonations: Array<ChartDataElementModel> = [];
                for (let dd of data.dailyDonations) {
                    sum += dd.amount;
                    cumulativeDonations.push({name: dd.date, value: sum});
                }
                this.lineChartData = [{name: '', series: cumulativeDonations}];
                console.log('lineCharData: ' + this.lineChartData);
            },
            (error) => {
                console.log(error);
                this.router.navigate(['page-not-found']);
            },
        );
    }
}
