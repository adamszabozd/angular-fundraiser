import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
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

    constructor(private router: Router, private fundsService: FundsService) {
    }

    ngOnInit() {
        this.fundsService.fetchAllFunds().subscribe(
            (data) => this.fundList = data,
            (error) => console.log(error),
        );
    }
}
