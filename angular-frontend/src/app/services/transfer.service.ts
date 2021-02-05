import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TransferFormInitDataModel} from '../models/transferFormInitData.model';
import {TransferCreationModel} from '../models/transferCreation.model';
import {environment} from "../../environments/environment";

const host = environment.BASE_URL;
const BASE_URL = host+'/api/transfers';

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

    deletePendingTransfer(id: number): Observable<any> {
        return this.http.delete(BASE_URL + '/' + id);
    }

}
