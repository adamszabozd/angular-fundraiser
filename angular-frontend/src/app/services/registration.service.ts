import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class RegistrationService {

    userRegistered = new Subject();

}
