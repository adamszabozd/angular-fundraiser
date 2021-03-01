import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {FundsService} from '../../services/funds.service';
import {ActivatedRoute, Router} from '@angular/router';
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

    form = this.formBuilder.group({
                                      title           : ['', Validators.required],
                                      shortDescription: ['', [Validators.required, Validators.maxLength(250)]],
                                      longDescription : [''],
                                      category        : [''],
                                      targetAmount    : ['', Validators.required],
                                      currency        : [''],
                                      endDate         : [''],
                                      status          : [''],
                                  });
    chosenImage: boolean = false;

    public tools: object = {
        items: ['Undo', 'Redo', '|',
            'Bold', 'Italic', 'Underline', 'StrikeThrough', '|',
            'FontName', 'FontSize', 'FontColor', 'BackgroundColor', '|',
            'SubScript', 'SuperScript', '|',
            'LowerCase', 'UpperCase', '|',
            'Formats', 'Alignments', '|', 'OrderedList', 'UnorderedList', '|',
            'Indent', 'Outdent', '|', 'CreateLink','CreateTable',
            '|', 'ClearFormat', 'SourceCode', '|', 'FullScreen']
    };

    constructor(private activatedRoute: ActivatedRoute, private fundService: FundsService, private router: Router, private formBuilder: FormBuilder) {
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
        this.form.get('category').setValue(data.category);
        this.form.get('targetAmount').setValue(data.targetAmount);
        this.form.get('currency').setValue(data.currency);
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

    reduceSpaces(s: string) {
        return s.trim().replace(/\s+/g, ' ');
    }

    reduceSpacesInShortDescription() {
        let shortDescription = this.form.get('shortDescription');
        shortDescription.setValue(this.reduceSpaces(shortDescription.value));
    }
}
