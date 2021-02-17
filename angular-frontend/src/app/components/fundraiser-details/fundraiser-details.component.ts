import {Component, OnInit} from '@angular/core';
import {FundsService} from "../../services/funds.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FundListItemModel} from "../../models/FundListItem.model";
import {FundDetailsItemModel} from "../../models/fundDetailsItem.model";

@Component({
    selector: 'app-fundraiser-details',
    templateUrl: './fundraiser-details.component.html',
    styleUrls: ['./fundraiser-details.component.css']
})
export class FundraiserDetailsComponent implements OnInit {

    id: number;
    fund: FundDetailsItemModel | undefined;

    constructor(private fundService: FundsService, private activatedRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {

        this.activatedRoute.paramMap.subscribe(
            (paraMap) => {
                this.id = Number.parseInt(paraMap.get("id"));
                this.fundService.fetchFundDetails(this.id).subscribe(
                    (data) => this.fund = data,
                    (error) => {
                        console.log(error);
                        this.router.navigate(['page-not-found']);
                    }
                )
            }
        )
    }

}
