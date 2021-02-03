import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {TransferService} from '../../services/transfer.service';

import {validationHandler} from '../../utils/validationHandler';
import {TransferFormInitDataModel} from '../../models/transferFormInitData.model';

@Component({
               selector   : 'app-transfer-funds',
               templateUrl: './transfer-funds.component.html',
               styleUrls  : ['./transfer-funds.component.css'],
           })
export class TransferFundsComponent implements OnInit {

    transferFormInitDataModel: TransferFormInitDataModel;
    submitted = false;

    form = this.formBuilder.group({
                                      targetFundId: [null, Validators.required],
                                      amount      : [null, [Validators.required, Validators.min(50), Validators.max(1000)]],
                                  });

    constructor(private formBuilder: FormBuilder, private transferService: TransferService, private router: Router) {
    }

    ngOnInit() {
        this.transferService.getNewTransferData().subscribe(
            (transferData) => {
                this.transferFormInitDataModel = transferData;
            },
            console.warn,
        );
    }

    submitForm() {
        this.transferService.submitTransfer(this.form.value).subscribe(
            () => this.submitted = true,
            error => validationHandler(error, this.form),
        );
    }

}
