import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FundListItemModel} from "../models/FundListItem.model";

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
}
