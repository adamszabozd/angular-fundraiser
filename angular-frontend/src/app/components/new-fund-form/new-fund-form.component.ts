import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {FundsService} from "../../services/funds.service";
import {Router} from "@angular/router";
import {CategoryOptionModel} from "../../models/categoryOption.model";
import {validationHandler} from "../../utils/validationHandler";
import {AccountService} from "../../services/account.service";
import {formAppearAnimation} from '../../animations';

@Component({
    selector: 'app-new-fund-form',
    templateUrl: './new-fund-form.component.html',
    styleUrls: ['./new-fund-form.component.css'],
    animations: [
        formAppearAnimation
    ]
})
export class NewFundFormComponent implements OnInit {

    state = "invisible";

    categories: CategoryOptionModel[];

    form = this.formBuilder.group({
        title: ['', Validators.required, Validators.minLength(5), Validators.maxLength(100)],
        shortDescription: ['', Validators.required, Validators.maxLength(250)],
        longDescription: [''],
        imageUrl: ['', Validators.maxLength(1000)],
        category: [null],
        targetAmount: [null, Validators.required],
        endDate: [null]
    })

    constructor(private formBuilder: FormBuilder, private fundService: FundsService, private router: Router, private accountService: AccountService) {
    }

    ngOnInit() {
        if (this.state == "invisible") {
            setTimeout(() => this.state = "visible")
        }
        this.fundService.getInitialFormData().subscribe(
            (data) => {
                this.categories = data;
                },
            (error) => console.log(error)
        );
        this.accountService.isLoggedIn().subscribe(
            loggedIn => {
                if (!loggedIn) {
                    this.accountService.loggedInStatusUpdate.next(false);
                    this.router.navigate(['/login']);
                }
            }
        );
    }

    onSubmit(){
        this.fundService.saveNewFund(this.form.value).subscribe(
            ()=> this.router.navigate(['']),
            (error)=> validationHandler(error, this.form)
        );
    }

}
