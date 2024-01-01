import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/User';

@Injectable({
  providedIn: 'root'
})
export class AdminManageAccountsService {


  private accountsUrl = 'http://localhost:8080/admins/waiting-requests/';

  //HARD-KODOVAN admin id
  private accountDisableUrl = 'http://localhost:8080/admins/disable-users/1/';

  //HARD-KODOVAN admin id
  private accountEnableUrl = 'http://localhost:8080/admins/enable-users/1/';

  private userByIdUrl = 'http://localhost:8080/admins/users/';

  private roomsUrl = 'http://localhost:8080/rooms';

  private permissionsUrl = 'http://localhost:8080/permissions';

  constructor(private http:HttpClient) { }


  getWaitingRequests(id: any): Observable<any> {
    const url = this.accountsUrl + id;
    return this.http.get(`${url}`);
  }

  getUserById(id: any): Observable<any> {
    const url = this.userByIdUrl + id;
    return this.http.get(`${url}`);
  }

  terminateUser(user: User): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = this.accountDisableUrl + user.id;
    return this.http.patch(`${url}`, JSON.stringify(user), {headers});
  }

  allowUser(user: User) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = this.accountEnableUrl + user.id;
    return this.http.patch(`${url}`, JSON.stringify(user), {headers});
  }

  getRooms() : Observable<any> {
    return this.http.get(`${this.roomsUrl}`);
  }

  addPermissions(permissions: any) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.permissionsUrl}`, JSON.stringify(permissions), {headers});
  }

}
