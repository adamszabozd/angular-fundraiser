import {Component, OnInit} from '@angular/core';
import {FundsService} from '../../services/funds.service';
import {FundListItemModel} from '../../models/fundListItem.model';
import {Router} from '@angular/router';
import {AccountService} from '../../services/account.service';
import {numberToString} from '../../utils/numberFormatter';
import {slideInFromDown} from '../../animations';

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
    numberToString = numberToString;

    constructor(private fundService: FundsService, private router: Router, private accountService: AccountService) {
    }

    ngOnInit() {
        this.fundService.fetchMyFunds().subscribe(
            (data) => this.myFundList = data,
            error => {
                this.accountService.loggedInStatusUpdate.next(false);
                this.router.navigate(['/login']);
                console.log(error);
            },
        );
    }
}
