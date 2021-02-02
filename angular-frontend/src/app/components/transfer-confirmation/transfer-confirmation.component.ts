import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {TransferService} from "../../services/transfer.service";
import {Router} from "@angular/router";
import {validationHandler} from "../../utils/validationHandler";

@Component({
  selector: 'app-transfer-confirmation',
  templateUrl: './transfer-confirmation.component.html',
  styleUrls: ['./transfer-confirmation.component.css']
})
export class TransferConfirmationComponent implements OnInit {

    confirmed = false;
    form = this.formBuilder.group({
        confirmationCode: ['', Validators.required],
    });

    constructor(private formBuilder: FormBuilder, private transferService: TransferService, private router: Router) { }

    ngOnInit() {
        if (!localStorage.auth) {
            this.router.navigate(['/registration']);
        }
    }

    submitForm() {
        this.transferService.confirmTransfer(this.form.value).subscribe(
            () => this.confirmed = true,
            error => validationHandler(error, this.form),
        );
    }

}
