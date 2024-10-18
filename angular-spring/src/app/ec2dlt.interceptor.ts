import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class Ec2dltInterceptor implements HttpInterceptor {

  constructor(public authSrv: AuthService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const userToken = this.authSrv.getToken();
    const isTokenValid = this.authSrv.isTokenValid();

    if (userToken && isTokenValid) {
      const modifiedReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${userToken}`),
      });
      return next.handle(modifiedReq);
    }


    // If token is missing or expired, send the request without it.
    return next.handle(req);
  }
}
