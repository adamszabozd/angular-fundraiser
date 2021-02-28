import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {FundListItemModel} from '../models/fundListItem.model';
import {CategoryOptionModel} from '../models/categoryOption.model';
import {FundFormCommandModel} from '../models/fundFormCommand.model';
import {FundFormInitModel} from '../models/fundFormInit.model';
import {FundDetailsItemModel} from '../models/fundDetailsItem.model';
import {TranslateService} from '@ngx-translate/core';
import {ModifyFundFormInitModel} from '../models/modifyFundFormInit.model';

const host = environment.BASE_URL;
const BASE_URL = host + '/api/funds';

@Injectable({
                providedIn: 'root',
            })
export class FundsService {

    languageStatusUpdate = new Subject<boolean>();

    constructor(private http: HttpClient, public translate: TranslateService) {
    }

    fetchAllFunds(): Observable<Array<FundListItemModel>> {
        return this.http.get<Array<FundListItemModel>>(BASE_URL);
    }

    fetchFundDetails(id: number): Observable<FundDetailsItemModel> {
        return this.http.get<FundDetailsItemModel>(BASE_URL + '/' + id);
    }

    fetchMyFunds(): Observable<Array<FundListItemModel>> {
        return this.http.get<Array<FundListItemModel>>(BASE_URL + '/myFunds');
    }

    fetchMyFundDetails(id: number): Observable<FundDetailsItemModel> {
        return this.http.get<FundDetailsItemModel>(BASE_URL + '/myFunds/' + id);
    }

    fetchFundForModify(id: number): Observable<ModifyFundFormInitModel> {
        return this.http.get<ModifyFundFormInitModel>(BASE_URL + '/modify/' + id);
    }

    modifyFund(data: FundFormCommandModel): Observable<any> {
        return this.http.put(BASE_URL + '/modify/', data);
    }

    saveNewFund(data: FormData): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    getInitialFormData(): Observable<FundFormInitModel> {
        return this.http.get<FundFormInitModel>(BASE_URL + '/initData');
    }

    getCategories(): Observable<CategoryOptionModel[]> {
        return this.http.get<CategoryOptionModel[]>(BASE_URL + '/categories', {
            params: new HttpParams()
                .set('lang', this.translate.currentLang),
        });
    }

    fetchFundsByCategory(category: string): Observable<Array<FundListItemModel>> {
        return this.http.get<Array<FundListItemModel>>(BASE_URL + '/categories/' + category);
    }
}
