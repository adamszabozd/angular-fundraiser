import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TransferService } from '../../services/transfer.service';

@Component({
    selector: 'app-summary-page',
    templateUrl: './summary-page.component.html',
    styleUrls: ['./summary-page.component.css'],
})
export class SummaryPageComponent implements OnInit, OnDestroy {

    accounts: Array<AccountDetailsModel>;
    transfers: Array<TransferDataModel>;
    intervalId: number;

    constructor(private accountService: AccountService, private transferService: TransferService, private router: Router) { }

    ngOnInit() {
        if (!localStorage.auth) {
            this.router.navigate(['/registration']);
        } else {
            this.fetchAccountsAndTransfers();
            this.intervalId = setInterval(() => this.fetchAccountsAndTransfers(), 10000);
        }
    }

    private fetchAccountsAndTransfers() {
        this.accountService.fetchAllAccounts().subscribe(
            accounts => this.accounts = accounts,
            console.warn,
        );
        this.transferService.fetchAllTransfers().subscribe(
            transfers => this.transfers = transfers,
            console.warn,
        );
    }

    ngOnDestroy() {
        clearInterval(this.intervalId);
    }
}
