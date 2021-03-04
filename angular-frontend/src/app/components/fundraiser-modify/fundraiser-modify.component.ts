import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {FundsService} from '../../services/funds.service';
import {ActivatedRoute, Router} from '@angular/router';
import {validationHandler} from '../../utils/validationHandler';
import {ModifyFundFormInitModel} from '../../models/modifyFundFormInit.model';
import {formAppearAnimation} from '../../animations';

@Component({
               selector   : 'app-fundraiser-modify',
               templateUrl: './fundraiser-modify.component.html',
               styleUrls  : ['./fundraiser-modify.component.css'],
               animations : [
                   formAppearAnimation,
               ],
           })
export class FundraiserModifyComponent implements OnInit {

    state = 'invisible';
    id: number;
    formData: ModifyFundFormInitModel;
    wrongImageFile: boolean = false;
    chosenImage: boolean = false;

    form = this.formBuilder.group({
                                      title           : ['', Validators.required],
                                      shortDescription: ['', [Validators.required, Validators.maxLength(250)]],
                                      longDescription : [''],
                                      imageFile       : [null],
                                      oldImageUrl     : [''],
                                      category        : [''],
                                      targetAmount    : ['', Validators.required],
                                      currency        : [''],
                                      endDate         : [''],
                                      status          : [''],
                                  });

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
        this.fundService.languageStatusUpdate.subscribe(
            data => {
                this.formData.statusOptions = data.statusOptions;
                this.formData.categoryOptions = data.categoryOptions;
                this.form.get('category').setValue(this.fundService.getCategoryDisplayName(this.formData.category, data.categoryOptions));
            }
        );
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            (paraMap) => {
                this.id = Number.parseInt(paraMap.get('id'));
                this.fundService.fetchFundForModify(this.id).subscribe(
                    (data) => {
                        if (this.state == 'invisible') {
                            setTimeout(() => this.state = 'visible');
                        }
                        this.fillForm(data);
                        this.formData = data;
                    },
                    (error) => {
                        console.log(error);
                        this.router.navigate(['page-not-found']);
                        if (this.state == 'invisible') {
                            setTimeout(() => this.state = 'visible');
                        }
                    },
                );
            },
        );
    }

    fillForm(data: ModifyFundFormInitModel) {
        this.form.get('title').setValue(data.title);
        this.form.get('shortDescription').setValue(data.shortDescription);
        this.form.get('longDescription').setValue(data.longDescription);
        this.form.get('oldImageUrl').setValue(data.oldImageUrl);
        this.form.get('category').setValue(this.fundService.getCategoryDisplayName(data.category, data.categoryOptions));
        this.form.get('targetAmount').setValue(data.targetAmount);
        this.form.get('currency').setValue(data.currency);
        this.form.get('endDate').setValue(data.endDate);
        this.form.get('status').setValue(data.status);
    }


    onFileChange($event: Event) {
        // @ts-ignore
        if (event.target.files.length > 0) {
            // @ts-ignore
            const file: File = event.target.files[0];

            if (file.name.match(/\.(gif|jpe?g|tiff?|png|webp|bmp)$/i) !== null && file.size <= 8388608) {
                this.form.patchValue({
                    imageFile: file,
                });
                this.wrongImageFile = false;
                this.chosenImage = true;
            } else {
                this.wrongImageFile = true;

            }
        }
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('title', this.form.get('title').value);
        formData.append('modifiedShortDescription', this.form.get('shortDescription').value);
        formData.append('modifiedLongDescription', this.form.get('longDescription').value);
        if (this.form.get('imageFile').value !== null) {
            formData.append('modifiedImageFile', this.form.get('imageFile').value);
        }
        formData.append('oldImageUrl', this.form.get('oldImageUrl').value);
        formData.append('modifiedCategory', this.form.get('category').value);
        formData.append('modifiedTargetAmount', this.form.get('targetAmount').value);
        formData.append('currency', this.form.get('currency').value);
        if (this.form.get('endDate').value !== null) {
            formData.append('modifiedEndDate', this.form.get('endDate').value);
        }
        formData.append('modifiedStatus', this.form.get('status').value);

        this.fundService.modifyFund(formData).subscribe(
            () => this.router.navigate(['/my-funds']),
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
