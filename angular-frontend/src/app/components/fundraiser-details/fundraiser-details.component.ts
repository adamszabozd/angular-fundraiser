import {Component, OnInit} from '@angular/core';
import {FundsService} from "../../services/funds.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FundDetailsItemModel} from "../../models/fundDetailsItem.model";
import {ChartDataElementModel} from "../../models/chartDataElement.model";

@Component({
    selector: 'app-fundraiser-details',
    templateUrl: './fundraiser-details.component.html',
    styleUrls: ['./fundraiser-details.component.css']
})
export class FundraiserDetailsComponent implements OnInit {

    id: number;
    fund: FundDetailsItemModel | undefined;
    barChartData: Array<ChartDataElementModel> | undefined;

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
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
    };
    view: any[] = [400, 250];

    constructor(private fundService: FundsService, private activatedRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {

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
        this.fundService.fetchFundDetails(this.id).subscribe(
            (data) => {
                this.fund = data;
                this.barChartData = data.lastWeekDonations.map(x => {
                    return {name: x.date, value: x.amount}
                });
            },
            (error) => {
                console.log(error);
                this.router.navigate(['page-not-found']);
            }
        )
    }

}
