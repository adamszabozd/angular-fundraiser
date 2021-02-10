import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FundListItemModel} from "../models/FundListItem.model";
import {CategoryOptionModel} from "../models/categoryOption.model";
import {FundFormCommandModel} from "../models/fundFormCommand.model";

const host = environment.BASE_URL;
const BASE_URL = host+'/api/funds';

@Injectable({
  providedIn: 'root'
})
export class FundsService {

  constructor(private http: HttpClient) { }

  fetchAllFunds(): Observable<Array<FundListItemModel>> {
      return this.http.get<Array<FundListItemModel>>(BASE_URL);
  }

    getInitialFormData(): Observable<CategoryOptionModel[]> {
        return this.http.get<CategoryOptionModel[]>(BASE_URL + "/initData")
    }

    saveNewFund(data: FundFormCommandModel): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    fetchSingleFund(id: number): Observable<FundListItemModel> {
        return this.http.get<FundListItemModel>(BASE_URL + "/" + id);
    }
}
