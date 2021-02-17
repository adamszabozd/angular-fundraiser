import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
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
    showOnlyOneOption: boolean = false;
    transferFormInitDataModel: TransferFormInitDataModel | undefined;

    form = this.formBuilder.group({
                                      targetFundId: [null, Validators.required],
                                      amount      : [null, [Validators.required]],
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
            (paraMap)=> {
                const id = paraMap.get('id');
                if(id){
                    this.transferService.getConcreteTransferData(id).subscribe(
                        (data)=> {
                            this.showOnlyOneOption = true;
                            this.transferFormInitDataModel = data;
                            this.form.patchValue({
                                targetFundId: data.targetFundOptions[0].id
                            })
                        },
                        (error)=> {
                            this.accountService.loggedInStatusUpdate.next(false);
                            this.router.navigate(['/login']);
                            console.log(error)
                        }
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
            }
        )


    }

    submitForm() {
        this.transferService.submitTransfer(this.form.value).subscribe(
            () => this.router.navigate(['/transfer-confirmation']),
            error => validationHandler(error, this.form),
        );
    }

}
