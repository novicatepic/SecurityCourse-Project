import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUNPW = environment.loginUNPW;

  private loginCode = environment.loginCode;

  constructor(private http:HttpClient) { }

  loginUserUPW(user: object): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.loginUNPW}`, JSON.stringify(user), {headers});
  }

  loginUserCode(code: object): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.loginCode}`, JSON.stringify(code), {headers});
  }
}
