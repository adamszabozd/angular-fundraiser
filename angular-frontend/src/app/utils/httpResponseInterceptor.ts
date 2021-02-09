import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {tap} from "rxjs/operators";

@Injectable()
export class HttpResponseInterceptor implements HttpInterceptor {

    constructor(private router: Router) {
    }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(tap(event => {
            if (event instanceof HttpResponse && this.isServerError(event.status)) {
                this.router.navigate(['page-not-found'])
            }
        }))
    }


    isServerError(serverStatus: number): boolean {
        return serverStatus >= 500 && serverStatus < 600;
    }
}
