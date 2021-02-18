import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {AccountDetailsModel} from '../models/accountDetails.model';
import {AccountRegistrationDataModel} from '../models/accountRegistrationData.model';
import {environment} from "../../environments/environment";
import {BalanceFormCommandModel} from '../models/balanceFormCommand.model';
import {CurrencyOptionModel} from '../models/currencyOption.model';

const host = environment.BASE_URL;
const BASE_URL = host + '/api/accounts';

@Injectable({providedIn: 'root'})
export class AccountService {

    loggedInStatusUpdate = new Subject<boolean>();

    constructor(private http: HttpClient) {
    }

    registerNewAccount(data: AccountRegistrationDataModel): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    login(credentials): Observable<any> {

        const headers = new HttpHeaders(credentials ? {
            authorization: 'Basic ' + btoa(credentials.email + ':' + credentials.password),
        } : {});

        return this.http.get(BASE_URL + '/me', {headers: headers});
    }

    getMyAccountDetails(): Observable<AccountDetailsModel> {
        return this.http.get<AccountDetailsModel>(BASE_URL + '/myAccountDetails');
    }

    // Ezt max akkor fogjuk használni, ha csinálunk adminfelületet is. Mezei usereknek nem listázzuk ki az összes regisztáltat.
    fetchAllAccounts(): Observable<any> {
        return this.http.get(BASE_URL);
    }

    isLoggedIn(): Observable<any> {
        return this.http.get(BASE_URL + "/loggedIn");
    }

    logout(): Observable<any> {
        return this.http.get(BASE_URL + "/logout");
    }

    fillMyBalance(data: BalanceFormCommandModel): Observable<any> {
        return this.http.post(BASE_URL + "/balance", data);
    }

    getCurrencies(): Observable<CurrencyOptionModel[]> {
        return this.http.get<CurrencyOptionModel[]>(BASE_URL + '/currency');
    }
}
