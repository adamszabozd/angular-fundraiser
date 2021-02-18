import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {AccountDetailsModel} from '../../models/accountDetails.model';
import {TransferService} from '../../services/transfer.service';
import {Router} from '@angular/router';
import {slideInFromDown} from '../../animations';
import {FormBuilder, Validators} from '@angular/forms';
import {minAmount} from '../../validator';
import {BalanceFormCommandModel} from '../../models/balanceFormCommand.model';

@Component({
               selector   : 'app-account-page',
               templateUrl: './account-page.component.html',
               styleUrls  : ['./account-page.component.css'],
               animations : [
                   slideInFromDown,
               ],
           })
export class AccountPageComponent implements OnInit {

    state = 'up';

    accountDetails: AccountDetailsModel | undefined;

    chosenData: AccountDetailsModel;

    setCurrency: boolean = false;
    setBalance: boolean = false;
    form = this.formBuilder.group({
                                      add_amount: ['', [Validators.required]],
                                  }, {
                                      validator: minAmount('add_amount'),
                                  });

    constructor(private accountService: AccountService, private transferService: TransferService, private router: Router, private formBuilder: FormBuilder) {
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
            },
        );
    }

    deletePendingTransfer(id: number): void {
        this.transferService.deletePendingTransfer(id).subscribe(
            data => this.accountDetails.pendingTransfers = data,
            error => console.log(error),
        );
    }

    resendConfirmationEmail(id: number): void {
        this.transferService.resendConfirmationEmail(id).subscribe(
            () => this.router.navigate(['/transfer-confirmation']),
            error => console.log(error),
        );
    }

    fillBalance() {
        this.setBalance = true;
        this.setCurrency = false;
    }

    handleBalance() {
        const data: BalanceFormCommandModel = {
            id       : this.accountDetails.id,
            addAmount: this.form.value.add_amount,
        };
        this.setBalance = false;
        this.accountService.fillMyBalance(data).subscribe(
            () => {
                this.setBalance = false;
            },
            error => console.log(error),
        );
    }

    changeCurrency() {
        this.setCurrency = true;
        this.setBalance = false;
    }
}
