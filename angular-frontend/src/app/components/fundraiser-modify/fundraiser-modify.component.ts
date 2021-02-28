import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FundsService} from '../../services/funds.service';
import {FormBuilder, Validators} from '@angular/forms';
import {validationHandler} from '../../utils/validationHandler';
import {ModifyFundFormInitModel} from '../../models/modifyFundFormInit.model';

@Component({
               selector   : 'app-fundraiser-modify',
               templateUrl: './fundraiser-modify.component.html',
               styleUrls  : ['./fundraiser-modify.component.css'],
           })
export class FundraiserModifyComponent implements OnInit {

    state = 'invisible';
    id: number;
    formData: ModifyFundFormInitModel;

    form = this.FormBuilder.group({
                                      title           : ['', Validators.required],
                                      shortDescription: ['', [Validators.required, Validators.maxLength(250)]],
                                      longDescription : [''],
                                      imageUrl        : ['', Validators.maxLength(1000)],
                                      category        : [''],
                                      targetAmount    : ['', Validators.required],
                                      endDate         : [''],
                                      status          : [''],
                                  });

    constructor(private activatedRoute: ActivatedRoute, private fundService: FundsService, private router: Router, private FormBuilder: FormBuilder) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            (paraMap) => {
                this.id = Number.parseInt(paraMap.get('id'));
                this.fundService.fetchFundForModify(this.id).subscribe(
                    (data) => {
                        this.fillForm(data);
                        this.formData = data;
                    },
                    (error) => {
                        console.log(error);
                        this.router.navigate(['page-not-found']);
                    },
                );
            },
        );
    }

    fillForm(data: ModifyFundFormInitModel) {
        this.form.get('title').setValue(data.title);
        this.form.get('shortDescription').setValue(data.shortDescription);
        this.form.get('longDescription').setValue(data.longDescription);
        this.form.get('imageUrl').setValue(data.imageUrl);
        this.form.get('category').setValue(data.category);
        this.form.get('targetAmount').setValue(data.targetAmount);
        this.form.get('endDate').setValue(data.endDate);
        this.form.get('status').setValue(data.status);
    }

    onSubmit() {
        this.fundService.modifyFund({
                                        ...this.form.value,
                                        id: this.id,
                                    }).subscribe(
            () => this.router.navigate(['my-funds']),
            (error) => validationHandler(error, this.form),
        );
    }
}
