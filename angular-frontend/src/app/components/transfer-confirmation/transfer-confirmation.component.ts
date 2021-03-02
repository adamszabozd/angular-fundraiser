import {Component, OnInit} from '@angular/core';
import {TransferService} from '../../services/transfer.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, Validators} from "@angular/forms";
import {formAppearAnimation} from '../../animations';
import {AccountService} from "../../services/account.service";

@Component({
               selector   : 'app-transfer-confirmation',
               templateUrl: './transfer-confirmation.component.html',
               styleUrls  : ['./transfer-confirmation.component.css'],
               animations : [
                   formAppearAnimation,
               ],
           })
export class TransferConfirmationComponent implements OnInit {

    state = 'invisible';
    confirmed = false;
    urlCode = false;
    transferId: string;
    errorMessage: string;
    form = this.formBuilder.group({
        confirmationCode: ['', Validators.required],
    });

    constructor(private transferService: TransferService,
                private router: Router,
                private route: ActivatedRoute,
                private formBuilder: FormBuilder,
                private accountService: AccountService) {
    }

    ngOnInit() {
        const bc = new BroadcastChannel('confirmation_status_update');
        bc.onmessage = ev => {
            this.transferService.confirmationStatusSubject.next(ev.data);
        };
        this.accountService.isLoggedIn().subscribe(
            loggedIn => {
                if (!loggedIn) {
                    this.accountService.loggedInStatusUpdate.next(false);
                    this.router.navigate(['/login']);
                } else {
                    this.route.paramMap.subscribe(
                        paramMap => {
                            const confirmationCode = paramMap.get('code');
                            this.transferId = paramMap.get('id');
                            this.transferService.confirmationStatusUpdate.subscribe(
                                transferId => {
                                    if (this.transferId == transferId) {
                                        this.confirmed = true;
                                        this.errorMessage = '';
                                    }
                                }
                            );
                            if (confirmationCode) {
                                this.urlCode = true;
                                this.transferService.confirmTransfer({'id': parseInt(this.transferId), 'confirmationCode': confirmationCode}).subscribe(
                                    (transferId) => {
                                        this.confirmed = true;
                                        bc.postMessage(transferId);
                                        if (this.state == 'invisible') {
                                            setTimeout(() => this.state = 'visible');
                                        }
                                    },
                                    error => {
                                        console.warn(error);
                                        this.errorMessage = error.error.error;
                                        if (this.state == 'invisible') {
                                            setTimeout(() => this.state = 'visible');
                                        }
                                    }
                                );
                            } else {
                                this.transferService.transferIsConfirmed(parseInt(this.transferId)).subscribe(
                                    () => {
                                        if (this.state == 'invisible') {
                                            setTimeout(() => this.state = 'visible');
                                        }},
                                    error => {
                                        console.log(error);
                                        this.errorMessage = error.error.error;
                                        if (this.state == 'invisible') {
                                            setTimeout(() => this.state = 'visible');
                                        }}
                                );
                            }
                        },
                        error => {
                            console.warn(error);
                            if (this.state == 'invisible') {
                                setTimeout(() => this.state = 'visible');
                            }
                        }
                    );
                }
            }
        );
    }

    submitForm() {
        let data = this.form.value;
        data.id = this.transferId;
        this.transferService.confirmTransfer(data).subscribe(
            () => this.confirmed = true,
            error => {
                console.log('nnnnnnnnnnnnnn' + error);
                this.form.get('confirmationCode').setErrors({serverError: error.error.error});
            }
        );
    }

}
