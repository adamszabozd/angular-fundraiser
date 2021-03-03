import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {CategoryOptionModel} from '../models/categoryOption.model';
import {FundFormInitModel} from '../models/fundFormInit.model';
import {FundDetailsItemModel} from '../models/fundDetailsItem.model';
import {TranslateService} from '@ngx-translate/core';
import {ModifyFundFormInitModel} from '../models/modifyFundFormInit.model';
import {ModifyFundCommandModel} from '../models/modifyFundCommand.model';
import {FundPageDataModel} from "../models/fundPageData.model";
import {EnumDataModel} from "../models/enumData.model";
import {MyFundsDataModel} from "../models/myFundsData.model";

const host = environment.BASE_URL;
const BASE_URL = host + '/api/funds';

@Injectable({
                providedIn: 'root',
            })
export class FundsService {

    languageStatusUpdate = new Subject<EnumDataModel>();

    constructor(private http: HttpClient, public translate: TranslateService) {
    }

    fetchFundsPage(page: number, sortBy?: string, dir?: string, category?: string): Observable<FundPageDataModel> {
        let params = new HttpParams().set("page", String(page)).set("size", "6");
        if(sortBy) {
            params = params.append("sort", sortBy+","+dir);
        }
        if(category){
            return this.http.get<FundPageDataModel>(BASE_URL + "/list/" + category, {params: params})
        }
        return this.http.get<FundPageDataModel>(BASE_URL + "/list", {params: params});
    }



    fetchFundDetails(id: number): Observable<FundDetailsItemModel> {
        return this.http.get<FundDetailsItemModel>(BASE_URL + '/' + id);
    }

    fetchMyFunds(): Observable<MyFundsDataModel> {
        return this.http.get<MyFundsDataModel>(BASE_URL + '/myFunds');
    }

    fetchMyFundDetails(id: number): Observable<FundDetailsItemModel> {
        return this.http.get<FundDetailsItemModel>(BASE_URL + '/myFunds/' + id);
    }

    fetchFundForModify(id: number): Observable<ModifyFundFormInitModel> {
        return this.http.get<ModifyFundFormInitModel>(BASE_URL + '/modify/' + id);
    }

    modifyFund(data: ModifyFundCommandModel): Observable<any> {
        return this.http.put(BASE_URL + '/modify', data);
    }

    saveNewFund(data: FormData): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    getInitialFormData(): Observable<FundFormInitModel> {
        return this.http.get<FundFormInitModel>(BASE_URL + '/initData');
    }

    getEnums(): Observable<EnumDataModel> {
        return this.http.get<EnumDataModel>(BASE_URL + '/enumData', {
            params: new HttpParams()
                .set('lang', this.translate.currentLang),
        });
    }

    getPdf(id: number) {
        return this.http.get(BASE_URL + '/pdf/' + id, { responseType: 'blob' as 'json' });
    }

    getCategoryDisplayName(categoryName: String, categoryOptions: Array<CategoryOptionModel>) {
        let categoryDisplayName;
        for (let categoryOption of categoryOptions) {
            if (categoryName == categoryOption.name) {
                categoryDisplayName = categoryOption.displayName;
                break;
            }
        }
        return categoryDisplayName;
    }

}
