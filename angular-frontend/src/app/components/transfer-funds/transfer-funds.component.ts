import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TransferService} from '../../services/transfer.service';

import {validationHandler} from '../../utils/validationHandler';
import {TransferFormInitDataModel} from '../../models/transferFormInitData.model';
import {AccountService} from '../../services/account.service';
import {formAppearAnimation} from '../../animations';
import {minAmount} from '../../validator';
import {numberToString} from '../../utils/numberFormatter';

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
    showOnlyOneOption: boolean = false;
    transferFormInitDataModel: TransferFormInitDataModel | undefined;
    numberToString = numberToString;

    targetCurrency: string = '---';
    targetAmount: number = 0.00;

    form = this.formBuilder.group({
                                      targetFundId: [null, Validators.required],
                                      senderAmount: [null, [Validators.required]],
                                  }, {
                                      validator: minAmount('senderAmount'),
                                  });

    constructor(private formBuilder: FormBuilder,
                private transferService: TransferService,
                private router: Router,
                private accountService: AccountService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        if (this.state == 'invisible') {
            setTimeout(() => this.state = 'visible');
        }

        this.route.paramMap.subscribe(
            (paraMap) => {
                const id = paraMap.get('id');
                if (id) {
                    this.transferService.getConcreteTransferData(id).subscribe(
                        (data) => {
                            this.showOnlyOneOption = true;
                            this.transferFormInitDataModel = data;
                            this.form.patchValue({
                                                     targetFundId: data.targetFundOptions[0].title,
                                                 });
                            this.targetCurrency = data.targetFundOptions[0].targetCurrency;
                        },
                        (error) => {
                            this.accountService.loggedInStatusUpdate.next(false);
                            this.router.navigate(['/login']);
                            console.log(error);
                        },
                    );
                } else {
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
            },
        );
    }

    submitForm() {
        const myPendingTransfer = {
            targetFundId: this.getTargetId(this.form.value.targetFundId),
            senderAmount: this.form.value.senderAmount,
        };
        this.transferService.submitTransfer(myPendingTransfer).subscribe(
            id => this.router.navigate(['/transfer-confirmation/' + id]),
            error => validationHandler(error, this.form),
        );
    }

    getTargetCurrency(title) {
        for (let targetFundOption of this.transferFormInitDataModel.targetFundOptions) {
            if (title.target.value === targetFundOption.title) {
                this.targetCurrency = targetFundOption.targetCurrency;
            }
        }
        this.getTargetAmount(this.form.value.senderAmount);
    }

    getTargetAmount(senderAmount: number) {
        let senderCurrencyRate: number;
        let fundCurrencyRate: number;
        for (let currencyOption of this.transferFormInitDataModel.currencyOptions) {
            if (currencyOption.currencyName === this.transferFormInitDataModel.currency) {
                senderCurrencyRate = currencyOption.exchangeRate;
            }
            if (currencyOption.currencyName === this.targetCurrency) {
                fundCurrencyRate = currencyOption.exchangeRate;
            }
        }
        this.targetAmount = fundCurrencyRate / senderCurrencyRate * senderAmount;
    }

    getSenderAmount(senderAmount) {
        this.getTargetAmount(senderAmount.target.value);
    }

    getTargetId(title: string) {
        for (let targetFundOption of this.transferFormInitDataModel.targetFundOptions) {
            if (title === targetFundOption.title) {
                return targetFundOption.id;
            }
        }
    }
}
