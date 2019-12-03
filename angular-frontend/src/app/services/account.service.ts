import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8080/api/accounts';

@Injectable({providedIn: 'root'})
export class AccountService {

    constructor(private http: HttpClient) {}

    registerNewAccount(data: {username: string, goal: string}): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    getMyAccountDetails(): Observable<any> {
        return this.http.get(BASE_URL + '/myAccountDetails');
    }

    fetchAllAccounts(): Observable<any> {
        return this.http.get(BASE_URL);
    }
}
