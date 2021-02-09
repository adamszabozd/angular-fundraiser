import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {FundsService} from "../../services/funds.service";
import {Router} from "@angular/router";
import {CategoryOptionModel} from "../../models/categoryOption.model";

@Component({
    selector: 'app-new-fund-form',
    templateUrl: './new-fund-form.component.html',
    styleUrls: ['./new-fund-form.component.css']
})
export class NewFundFormComponent implements OnInit {

    categories: CategoryOptionModel[];

    form = this.formBuilder.group({
        title: ['', Validators.required],
        shortDescription: ['', Validators.required, Validators.minLength(10), Validators.maxLength(200)],
        longDescription: [''],
        imageUrl: [''],
        category: [null],
        targetAmount: [null, Validators.required],
        endDate: [null]
    })



    constructor(private formBuilder: FormBuilder, private fundService: FundsService, private router: Router) {
    }

    ngOnInit() {
        this.fundService.getInitialFormData().subscribe(
            (data)=> this.categories=data,
            (error) => console.log(error)
        )
    }

    onSubmit(){}

}
