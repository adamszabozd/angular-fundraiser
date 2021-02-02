import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TransferService } from '../../services/transfer.service';

import { validationHandler } from '../../utils/validationHandler';

@Component({
    selector: 'app-transfer-funds',
    templateUrl: './transfer-funds.component.html',
    styleUrls: ['./transfer-funds.component.css'],
})
export class TransferFundsComponent implements OnInit {

    sourceAccountName: string;
    balance: number;
    targetAccounts: Array<TargetAccountOptionModel> = [];
    form = this.formBuilder.group({
        target: [null, Validators.required],
        amount: [null, [Validators.required, Validators.min(50), Validators.max(1000)]],
    });

    constructor(private formBuilder: FormBuilder, private transferService: TransferService, private router: Router) { }

    ngOnInit() {
        if (!localStorage.auth) {
            this.router.navigate(['/registration']);
        } else {
            this.transferService.getNewTransferData().subscribe(
                transferData => {
                    this.sourceAccountName = transferData.sourceAccountName;
                    this.balance = transferData.balance;
                    this.targetAccounts = transferData.targetAccountOptions;
                },
                console.warn,
            );
        }
    }

    submitForm() {
        this.transferService.submitTransfer(this.form.value).subscribe(
            () => this.router.navigate(['/transfer-confirmation']),
            error => validationHandler(error, this.form),
        );
    }

}
