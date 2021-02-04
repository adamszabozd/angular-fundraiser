import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {environment} from "../../environments/environment";

const host = environment.BASE_URL;


@Injectable({providedIn: 'root'})
export class RegistrationService {

    userRegistered = new Subject();

}
