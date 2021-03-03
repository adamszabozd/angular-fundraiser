import {Component, OnInit} from '@angular/core';
import {FundsService} from '../../services/funds.service';
import {FundListItemModel} from '../../models/fundListItem.model';
import {Router} from '@angular/router';
import {AccountService} from '../../services/account.service';
import {numberToString} from '../../utils/numberFormatter';
import {slideInFromDown} from '../../animations';
import {CategoryOptionModel} from "../../models/categoryOption.model";

@Component({
               selector   : 'app-my-funds',
               templateUrl: './my-funds.component.html',
               styleUrls  : ['./my-funds.component.css'],
               animations : [
                   slideInFromDown,
               ],
           })
export class MyFundsComponent implements OnInit {

    state = 'up';
    myFundList: Array<FundListItemModel>;
    categoryOptions: Array<CategoryOptionModel>;
    numberToString = numberToString;

    constructor(public fundService: FundsService, private router: Router, private accountService: AccountService) {
        this.fundService.languageStatusUpdate.subscribe(
            data => {
                this.categoryOptions = data.categoryOptions;
            }
        );
    }

    ngOnInit() {
        this.fundService.fetchMyFunds().subscribe(
            (data) => {
                this.categoryOptions = data.categoryOptions;
                this.myFundList = data.fundListItems;
            },
            error => {
                this.accountService.loggedInStatusUpdate.next(false);
                this.router.navigate(['/login']);
                console.log(error);
            },
        );
    }
}
