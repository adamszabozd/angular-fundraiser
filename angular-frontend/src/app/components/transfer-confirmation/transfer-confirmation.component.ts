import {Component, OnInit} from '@angular/core';
import {TransferService} from '../../services/transfer.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, Validators} from "@angular/forms";
import {validationHandler} from "../../utils/validationHandler";

@Component({
               selector   : 'app-transfer-confirmation',
               templateUrl: './transfer-confirmation.component.html',
               styleUrls  : ['./transfer-confirmation.component.css'],
           })
export class TransferConfirmationComponent implements OnInit {

    confirmed = false;
    urlCode = false;
    form = this.formBuilder.group({
        confirmationCode: ['', Validators.required],
    });

    constructor(private transferService: TransferService,
                private router: Router,
                private route: ActivatedRoute,
                private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(
            paramMap => {
                const confirmationCode = paramMap.get('code');
                if (confirmationCode) {
                    this.urlCode = true;
                    this.transferService.confirmTransfer({'confirmationCode': confirmationCode}).subscribe(
                        () => this.confirmed = true,
                        error => console.warn(error),
                    );
                }
            },
            error => console.warn(error),
        );
    }

    submitForm() {
        this.transferService.confirmTransfer(this.form.value).subscribe(
            () => this.confirmed = true,
            error => validationHandler(error, this.form),
        );
    }

}
