import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {FundsService} from '../../services/funds.service';
import {Router} from '@angular/router';
import {validationHandler} from '../../utils/validationHandler';
import {AccountService} from '../../services/account.service';
import {formAppearAnimation} from '../../animations';
import {FundFormInitModel} from '../../models/fundFormInit.model';
import {invalid} from "@angular/compiler/src/render3/view/util";

@Component({
    selector: 'app-new-fund-form',
    templateUrl: './new-fund-form.component.html',
    styleUrls: ['./new-fund-form.component.css'],
    animations: [
        formAppearAnimation
    ]
})
export class NewFundFormComponent implements OnInit {

    state = 'invisible';

    wrongImageFile = false;

    formInitData: FundFormInitModel | undefined;

    form = this.formBuilder.group({
        title: ['', [Validators.required, Validators.pattern(/^[^\s]+(\s+[^\s]+)*$/)]],
        shortDescription: ['', [Validators.required, Validators.maxLength(250), Validators.pattern(/^[^\s]+(\s+[^\s]+)*$/)]],
        longDescription: [''],
        imageFile: [null, Validators.required],
        category: [null],
        targetAmount: [null, Validators.required],
        currency: [null, Validators.required],
        endDate: [null],
        status: ["ACTIVE"],
    });

    constructor(private formBuilder: FormBuilder, private fundService: FundsService, private router: Router, private accountService: AccountService) {
    }

    ngOnInit() {
        if (this.state == 'invisible') {
            //TODO - Review: Ilyet anti-pattern csinálni... setTimeout-ot lifecycle methodban... Nincs rá szebb megoldás?
            setTimeout(() => this.state = 'visible');
        }
        this.fundService.getInitialFormData().subscribe(
            (data) => {
                this.formInitData = data;
            },
            (error) => console.log(error),
        );
        this.accountService.isLoggedIn().subscribe(
            loggedIn => {
                if (!loggedIn) {
                    this.accountService.loggedInStatusUpdate.next(false);
                    this.router.navigate(['/login']);
                }
            },
        );
    }

    onSubmit() {

        const formData = new FormData();
        formData.append('title', this.form.get('title').value);
        formData.append('shortDescription', this.form.get('shortDescription').value);
        formData.append('longDescription', this.form.get('longDescription').value);
        formData.append('imageFile', this.form.get('imageFile').value);
        formData.append('category', this.form.get('category').value);
        formData.append('targetAmount', this.form.get('targetAmount').value);
        formData.append('currency', this.form.get('currency').value);
        formData.append('endDate', this.form.get('endDate').value);
        formData.append('status', this.form.get('status').value);

        this.fundService.saveNewFund(formData).subscribe(
            () => this.router.navigate(['/my-funds']),
            (error) => validationHandler(error, this.form)
        );
    }

    onFileChange($event: Event) {
        // @ts-ignore
        if (event.target.files.length > 0) {
            // @ts-ignore
            const file: File = event.target.files[0];

            if (file.name.match(/\.(gif|jpe?g|tiff?|png|webp|bmp)$/i) !== null) {
                this.form.patchValue({
                    imageFile: file
                });
                this.wrongImageFile = false;
            } else {
                this.wrongImageFile = true;
            }
        }
    }

    // uploadedFileValidator(control: FormControl) {
    //     if (this.form.get('imageFile').touched) {
    //         if (control.value.name.match(/\.(gif|jpe?g|tiff?|png|webp|bmp)$/i) !== null) {
    //             return {'wrongFile': true}
    //         }
    //     }
    //     return null;
    //     }
}
