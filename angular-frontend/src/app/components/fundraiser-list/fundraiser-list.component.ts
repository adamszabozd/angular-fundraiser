import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {FundsService} from '../../services/funds.service';
import {FundListItemModel} from '../../models/FundListItem.model';
import {slideInFromDown} from '../../animations';

@Component({
               selector   : 'app-summary-page',
               templateUrl: './fundraiser-list.component.html',
               styleUrls  : ['./fundraiser-list.component.css'],
               animations : [
                   slideInFromDown,
               ],
           })
export class FundraiserListComponent implements OnInit {

    state = 'up';

    fundList: Array<FundListItemModel>;
    numberOfFunds: number;
    page: number = 1;
    paramMap: ParamMap;

    constructor(private router: Router, private fundsService: FundsService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(
            paramMap => {
                this.paramMap = paramMap;
                this.fetchData();
            }
        )
        this.fundsService.languageStatusUpdate.subscribe(() => {
            this.fetchData();
        })

    }

    fetchData() {
        const category = this.paramMap.get('category');
        if(category){
            this.fundsService.fetchFundsByCategory(category).subscribe(
                (data)=> {
                    this.fundList = data;
                    this.numberOfFunds = data.length;
                },
                (error)=> console.log(error)
            );
        } else this.fundsService.fetchAllFunds().subscribe(
            (data) => {
                this.fundList = data
                this.numberOfFunds = data.length;
            },
            (error) => console.log(error),
        );
    }

    sortByRaisedFundsDesc() {
        console.log ("sortByRaisedAmount() called");
        this.fundList.sort(
            (a, b) => (a.raisedAmount > b.raisedAmount) ? -1 : 1
        );
    }

    sortByExpirationDateAsc() {
        console.log("sortByEndDate() called")
        this.fundList.sort(
            (a, b) => (a.endDate > b.endDate) ? 1 : -1
        );
    }

    sortByIdDesc() {
        this.fundList.sort(
            (a,b) => (a.id < b.id)? 1 : -1
        );
    }

    sortBYCreatorName(){
        this.fundList.sort(
            (a,b) => (a.creatorName > b.creatorName) ? 1 : -1
        );
    }
}
