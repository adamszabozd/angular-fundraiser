import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-account-page',
    templateUrl: './account-page.component.html',
    styleUrls: ['./account-page.component.css'],
})
export class AccountPageComponent implements OnInit {

    accountDetails: AccountDetailsModel = {
        username: '',
        goal: '',
        balance: null,
        funds: null,
    };
    sourceTransfers: Array<TransferDataModel>;
    targetTransfers: Array<TransferDataModel>;

    constructor(private accountService: AccountService, private router: Router) {}

    ngOnInit(): void {
        if (!localStorage.getItem('auth')) {
            this.router.navigate(['/registration']);
        } else {
            this.accountService.getMyAccountDetails().subscribe(
                data => {
                    this.accountDetails = {
                        username: data.username,
                        goal: data.goal,
                        balance: data.balance,
                        funds: data.funds,
                    };
                    this.sourceTransfers = data.sourceTransfers;
                    this.targetTransfers = data.targetTransfers;
                },
            );
        }
    }

}
