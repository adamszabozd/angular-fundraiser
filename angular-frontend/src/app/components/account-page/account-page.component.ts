import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';

@Component({
    selector: 'app-account-page',
    templateUrl: './account-page.component.html',
    styleUrls: ['./account-page.component.css'],
})
export class AccountPageComponent implements OnInit {

    accountDetails: AccountDetailsModel = {
        email: '',
        balance: null
    };
    outgoingTransfers: Array<TransferDataModel>;
    incomingTransfers: Array<TransferDataModel>;

    constructor(private accountService: AccountService, private router: Router) {}

    ngOnInit(): void {
            this.accountService.getMyAccountDetails().subscribe(
                data => {
                    this.accountDetails = {
                        email: data.email,
                        balance: data.balance,
                    };
                    this.outgoingTransfers = data.outgoingTransfers;
                    this.incomingTransfers = data.incomingTransfers;
                },
            );

    }

}
