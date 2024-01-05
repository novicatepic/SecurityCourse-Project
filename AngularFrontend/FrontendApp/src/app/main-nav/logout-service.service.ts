import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogoutServiceService {

  private logoutURL = environment.logoutUrl;


  constructor(private http:HttpClient) { }

  logoutUser(): Observable<any> {
    return this.http.get(`${this.logoutURL}`);
  }

}
