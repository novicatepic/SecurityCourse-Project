import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUNPW = 'http://localhost:8080/auth/login';

  private loginCode = 'http://localhost:8080/auth/code';

  private usernameUrl = 'http://localhost:4040/fitness-users/user/';

  constructor(private http:HttpClient) { }

  loginUserUPW(user: object): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.loginUNPW}`, JSON.stringify(user), {headers});
  }

  loginUserCode(code: object): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.loginCode}`, JSON.stringify(code), {headers});
  }

  getByUsername(username: any): Observable<any> {
    const url = this.usernameUrl + username;
    return this.http.get(`${url}`);
  }
}
