import {Injectable, NgZone} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {TransferFormInitDataModel} from '../models/transferFormInitData.model';
import {TransferCreationModel} from '../models/transferCreation.model';
import {environment} from "../../environments/environment";
import {runInZone} from "../utils/runInZone";

const host = environment.BASE_URL;
const BASE_URL = host+'/api/transfers';

@Injectable({providedIn: 'root'})
export class TransferService {
    confirmationStatusSubject = new Subject<string>();
    confirmationStatusUpdate: Observable<string>;

    constructor(private http: HttpClient, private ngZone: NgZone) {
        this.confirmationStatusUpdate = this.confirmationStatusSubject.pipe(runInZone(this.ngZone));
    }

    getNewTransferData(): Observable<TransferFormInitDataModel> {

            return this.http.get<TransferFormInitDataModel>(BASE_URL + '/' + 'newTransferData');

    }

    getConcreteTransferData(id: string): Observable<TransferFormInitDataModel> {
        return this.http.get<TransferFormInitDataModel>(BASE_URL + "/" + id);
    }

    submitTransfer(data: TransferCreationModel): Observable<number> {
        return this.http.post<number>(BASE_URL, data);
    }

    confirmTransfer(data: {id: number, confirmationCode: string}): Observable<number> {
        return this.http.post<number>(BASE_URL + '/' + 'confirm', data);
    }

    deletePendingTransfer(id: number): Observable<any> {
        return this.http.delete(BASE_URL + '/' + id);
    }

    resendConfirmationEmail(id: number): Observable<any> {
        return this.http.get(BASE_URL + '/resend/' + id);
    }

    fetchAllTransfers(): Observable<any> {
        return this.http.get(BASE_URL);
    }

    transferIsConfirmed(id: number): Observable<any> {
        return this.http.get(BASE_URL + '/confirmed/' + id);
    }

}
