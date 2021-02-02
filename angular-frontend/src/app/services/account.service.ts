import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8080/api/accounts';

@Injectable({providedIn: 'root'})
export class AccountService {

    constructor(private http: HttpClient) {}

    registerNewAccount(data: {email: string, password: string}): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    login(credentials): Observable<any> {

        const headers = new HttpHeaders(credentials ? {
            authorization: 'Basic ' + btoa(credentials.email + ':' + credentials.password),
        } : {});

        return this.http.get(BASE_URL + '/me', {headers: headers});
    }

    getMyAccountDetails(): Observable<any> {
        return this.http.get(BASE_URL + '/myAccountDetails');
    }

    // Ezt max akkor fogjuk használni, ha csinálunk adminfelületet is. Mezei usereknek nem listázzuk ki az összes regisztáltat.
    fetchAllAccounts(): Observable<any> {
        return this.http.get(BASE_URL);
    }
}
