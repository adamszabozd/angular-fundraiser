import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';
import {TransferService} from "../../services/transfer.service";

@Component({
               selector   : 'app-account-page',
               templateUrl: './account-page.component.html',
               styleUrls  : ['./account-page.component.css'],
           })
export class AccountPageComponent implements OnInit {

    accountDetails: AccountDetailsModel;

    constructor(private accountService: AccountService, private transferService: TransferService) {
    }

    ngOnInit(): void {
        this.accountService.getMyAccountDetails().subscribe(
            (data) => {
                this.accountDetails = data;
            },
        );

    }

    deletePendingTransfer(id: number): void {
        this.transferService.deletePendingTransfer(id).subscribe(
            data => this.accountDetails.pendingTransfers = data,
            error => console.log(error)
        );
    }

}
