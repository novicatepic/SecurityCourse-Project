import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JwtTokenService } from '../jwt-token/jwt-token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements HttpInterceptor  {

  constructor(private jwtService: JwtTokenService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    {
      let token = this.jwtService.extractToken();
      if (token) {
        if(this.jwtService.checkIfTokenExpired(token)) {
          //console.log("EXPIRED!");
        } else {
          //console.log("INTERCEPTED!");
          request = request.clone({
            setHeaders: {
              Authorization: `Bearer ${token}`
            }
          });
        }
        
      }
      return next.handle(request);
    }
    throw new Error('Method not implemented.');
  }

  
}
