import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {FundListItemModel} from "../models/FundListItem.model";
import {CategoryOptionModel} from "../models/categoryOption.model";
import {FundFormCommandModel} from "../models/fundFormCommand.model";
import {FundDetailsItemModel} from "../models/fundDetailsItem.model";
import {TranslateService} from "@ngx-translate/core";

const host = environment.BASE_URL;
const BASE_URL = host+'/api/funds';

@Injectable({
  providedIn: 'root'
})
export class FundsService {

    languageStatusUpdate = new Subject<boolean>();

    constructor(private http: HttpClient, public translate: TranslateService) { }

  fetchAllFunds(): Observable<Array<FundListItemModel>> {
      return this.http.get<Array<FundListItemModel>>(BASE_URL);
  }

    fetchMyFunds(): Observable<Array<FundListItemModel>> {
      return this.http.get<Array<FundListItemModel>>(BASE_URL+"/myFunds")
    }

    getInitialFormData(): Observable<CategoryOptionModel[]> {
        return this.http.get<CategoryOptionModel[]>(BASE_URL + "/initData", {
            params: new HttpParams()
                .set('lang', this.translate.currentLang)
        })
    }

    saveNewFund(data: FundFormCommandModel): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    modifyFund(data: FundFormCommandModel): Observable<any> {
        return this.http.put(BASE_URL, data);
    }


    fetchFundForModify(id: number): Observable<FundFormCommandModel> {
        return this.http.get<FundFormCommandModel>(BASE_URL + "/modify/" + id);
    }

    fetchFundDetails(id: number): Observable<FundDetailsItemModel> {
        return this.http.get<FundDetailsItemModel>(BASE_URL + "/" + id);
    }

    fetchFundsByCategory(category: string): Observable<Array<FundListItemModel>> {
        return this.http.get<Array<FundListItemModel>>(BASE_URL+"/categories/" + category);
    }
}
