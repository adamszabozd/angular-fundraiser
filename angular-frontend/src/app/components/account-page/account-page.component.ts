import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';

@Component({
               selector   : 'app-account-page',
               templateUrl: './account-page.component.html',
               styleUrls  : ['./account-page.component.css'],
           })
export class AccountPageComponent implements OnInit {

    accountDetails: AccountDetailsModel;

    constructor(private accountService: AccountService) {
    }

    ngOnInit(): void {
        this.accountService.getMyAccountDetails().subscribe(
            (data) => {
                this.accountDetails = data;
            },
        );

    }

}
