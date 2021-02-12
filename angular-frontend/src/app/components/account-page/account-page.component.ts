import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';
import {TransferService} from "../../services/transfer.service";
import {Router} from "@angular/router";

@Component({
               selector   : 'app-account-page',
               templateUrl: './account-page.component.html',
               styleUrls  : ['./account-page.component.css'],
           })
export class AccountPageComponent implements OnInit {

    accountDetails: AccountDetailsModel;

    constructor(private accountService: AccountService, private transferService: TransferService, private router: Router) {
    }

    ngOnInit(): void {
        this.accountService.getMyAccountDetails().subscribe(
            (data) => {
                this.accountDetails = data;
            },
            error => {
                this.accountService.loggedInStatusUpdate.next(false);
                this.router.navigate(['/login']);
                console.log(error);
            }
        );
    }

    deletePendingTransfer(id: number): void {
        this.transferService.deletePendingTransfer(id).subscribe(
            data => this.accountDetails.pendingTransfers = data,
            error => console.log(error)
        );
    }

    resendConfirmationEmail(id: number): void {
        this.transferService.resendConfirmationEmail(id).subscribe(
            () => this.router.navigate(['/transfer-confirmation']),
            error => console.log(error)
        );
    }

}
