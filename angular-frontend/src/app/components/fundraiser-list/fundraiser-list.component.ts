import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {FundsService} from '../../services/funds.service';
import {FundListItemModel} from '../../models/fundListItem.model';
import {slideInFromDown} from '../../animations';
import {numberToString} from '../../utils/numberFormatter';
import {CategoryOptionModel} from "../../models/categoryOption.model";

@Component({
               selector   : 'app-summary-page',
               templateUrl: './fundraiser-list.component.html',
               styleUrls  : ['./fundraiser-list.component.css'],
               animations : [
                   slideInFromDown,
               ],
           })
export class FundraiserListComponent implements OnInit {

    state = 'up';

    fundList: Array<FundListItemModel>;
    totalItems: number;
    categoryOptions: Array<CategoryOptionModel>;
    page: number = 1;
    paramMap: ParamMap;
    sortBy: string;
    dir: string;

    numberToString = numberToString;

    constructor(private router: Router, public fundsService: FundsService, private route: ActivatedRoute) {
        this.fundsService.languageStatusUpdate.subscribe(
            data => {
                this.categoryOptions = data.categoryOptions;
            }
        );
    }

    ngOnInit() {
        this.route.paramMap.subscribe(
            paramMap => {
                this.paramMap = paramMap;
                this.fetchData();
            },
        );
    }

    fetchData(sortBy?: string, dir?: string) {
        const category = this.paramMap.get('category');
        if (category) {
            this.page = 1;
            this.fundsService.fetchFundsPage(this.page-1, sortBy, dir, category).subscribe(
                (data) => {
                    this.categoryOptions = data.categoryOptions;
                    this.fundList = data.funds;
                    this.totalItems = data.count;
                },
                (error) => console.log(error),
            );
        } else {
            this.fundsService.fetchFundsPage(this.page-1, sortBy, dir).subscribe(
                (data) => {
                    this.categoryOptions = data.categoryOptions;
                    this.fundList = data.funds;
                    this.totalItems = data.count;
                },
                (error) => console.log(error),
            );
        }
    }

    sortByRaisedFundsDesc() {
        this.sortBy = undefined;
        this.dir = undefined;
        this.page = 1;
        this.fetchData(this.sortBy, this.dir);
    }

    sortByExpirationDateAsc() {
        this.sortBy = "endDate";
        this.dir = "asc";
        this.page = 1;
        this.fetchData(this.sortBy, this.dir);
    }

    sortByIdDesc() {
        this.sortBy = "id";
        this.dir = "desc";
        this.page = 1;
        this.fetchData(this.sortBy, this.dir)
    }

    sortByCreatorName() {
        this.sortBy = "creator.username";
        this.dir = "asc";
        this.page = 1;
        this.fetchData(this.sortBy, this.dir)
    }

    pageChanged($event: number) {
        this.page=$event;
        this.fetchData(this.sortBy, this.dir);

    }
}
