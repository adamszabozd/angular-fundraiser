import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {TransferService} from '../../services/transfer.service';

import {validationHandler} from '../../utils/validationHandler';
import {TransferFormInitDataModel} from '../../models/transferFormInitData.model';
import {AccountService} from '../../services/account.service';
import {formAppearAnimation} from '../../animations';

@Component({
               selector   : 'app-transfer-funds',
               templateUrl: './transfer-funds.component.html',
               styleUrls  : ['./transfer-funds.component.css'],
               animations : [
                   formAppearAnimation,
               ],
           })
export class TransferFundsComponent implements OnInit {

    state = 'invisible';
    transferFormInitDataModel: TransferFormInitDataModel | undefined;

    form = this.formBuilder.group({
                                      targetFundId: [null, Validators.required],
                                      amount      : [null, [Validators.required, Validators.min(50), Validators.max(1000)]],
                                  });

    constructor(private formBuilder: FormBuilder, private transferService: TransferService, private router: Router, private accountService: AccountService) {
    }

    ngOnInit() {
        if (this.state == 'invisible') {
            setTimeout(() => this.state = 'visible');
        }
        this.transferService.getNewTransferData().subscribe(
            (transferData) => {
                this.transferFormInitDataModel = transferData;
            },
            error => {
                this.accountService.loggedInStatusUpdate.next(false);
                this.router.navigate(['/login']);
                console.log(error);
            },
        );
    }

    submitForm() {
        this.transferService.submitTransfer(this.form.value).subscribe(
            () => this.router.navigate(['/transfer-confirmation']),
            error => validationHandler(error, this.form),
        );
    }

}
