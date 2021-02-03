import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TransferFormInitDataModel} from '../models/transferFormInitData.model';
import {TransferCreationModel} from '../models/transferCreation.model';

const BASE_URL = 'http://localhost:8080/api/transfers';

@Injectable({providedIn: 'root'})
export class TransferService {

    constructor(private http: HttpClient) {}

    fetchAllTransfers(): Observable<any> {
        return this.http.get(BASE_URL);
    }

    getNewTransferData(): Observable<TransferFormInitDataModel> {
        return this.http.get<TransferFormInitDataModel>(BASE_URL + '/' + 'newTransferData');
    }

    submitTransfer(data: TransferCreationModel): Observable<any> {
        return this.http.post(BASE_URL, data);
    }

    confirmTransfer(data: {confirmationCode: string}): Observable<any> {
        return this.http.post(BASE_URL + '/' + 'confirm', data);
    }

}
