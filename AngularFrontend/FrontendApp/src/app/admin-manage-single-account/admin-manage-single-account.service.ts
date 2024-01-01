import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/User';

@Injectable({
  providedIn: 'root'
})
export class AdminManageSingleAccountService {

  private accountUpdateUrl = 'http://localhost:8080/admins/update-role';


  constructor(private http:HttpClient) { }


  modifyUser(user: User): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.patch(`${this.accountUpdateUrl}`, JSON.stringify(user), {headers});
  }


}
