import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChooseAccountService {

  private url = environment.usersToModifyUrl;


  constructor(private http:HttpClient) { }


  getUsers() : Observable<any> {
    const url = this.url;
    return this.http.get(`${url}`);
  }
}
