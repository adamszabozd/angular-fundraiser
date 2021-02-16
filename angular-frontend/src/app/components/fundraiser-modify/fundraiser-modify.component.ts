import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FundsService} from "../../services/funds.service";
import {FundListItemModel} from "../../models/FundListItem.model";
import {FormBuilder, Validators} from "@angular/forms";
import {validationHandler} from "../../utils/validationHandler";

@Component({
    selector: 'app-fundraiser-modify',
    templateUrl: './fundraiser-modify.component.html',
    styleUrls: ['./fundraiser-modify.component.css']
})
export class FundraiserModifyComponent implements OnInit {

    state = "invisible";
    id: number;
    fund: FundListItemModel

    form = this.FormBuilder.group({
        title: ['', Validators.required],
        shortDescription: ['', [Validators.required, Validators.maxLength(250)]],
        longDescription: [''],
        imageUrl: ['', Validators.maxLength(1000)],
        category: [''],
        targetAmount: ['', Validators.required],
        endDate: ['']
    })

    constructor(private activatedRoute: ActivatedRoute, private fundService: FundsService, private router: Router, private FormBuilder: FormBuilder) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            (paraMap) => {
                this.id = Number.parseInt(paraMap.get("id"));
                this.fundService.fetchSingleFund(this.id).subscribe(
                    (data) => this.fillForm(data),
                    (error) => {
                        console.log(error);
                        this.router.navigate(['page-not-found']);
                    }
                )
            }
        )
    }

    fillForm(fundListItemModel: FundListItemModel){
        this.form.get("title").setValue(fundListItemModel.title);
        this.form.get("shortDescription").setValue(fundListItemModel.shortDescription);
        this.form.get("longDescription").setValue(fundListItemModel.longDescription);
        this.form.get("imageUrl").setValue(fundListItemModel.imageUrl);
        this.form.get("category").setValue(fundListItemModel.category);
        this.form.get("targetAmount").setValue(fundListItemModel.targetAmount);
        this.form.get("endDate").setValue(fundListItemModel.endDate)
    }

    onSubmit() {
        this.fundService.modifyFund({
            ...this.form.value,
            id: this.id
        }).subscribe(
            ()=> this.router.navigate(['']),
            (error)=> validationHandler(error, this.form)
        );
    }
}
