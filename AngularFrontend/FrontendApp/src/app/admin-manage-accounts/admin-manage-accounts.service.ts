import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/User';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminManageAccountsService {


  private accountsUrl = environment.accountsUrl;

  private accountDisableUrl = environment.accountDisableUrl;

  private accountEnableUrl = environment.accountEnableUrl;

  private userByIdUrl = environment.userByIdUrl;

  private roomsUrl = environment.roomsUrl;

  private permissionsUrl = environment.permissionsUrl;

  constructor(private http:HttpClient) { }


  getWaitingRequests(id: any): Observable<any> {
    const url = this.accountsUrl + id;
    return this.http.get(`${url}`);
  }

  getUserById(id: any): Observable<any> {
    const url = this.userByIdUrl + id;
    return this.http.get(`${url}`);
  }

  terminateUser(user: User, adminId: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = this.accountDisableUrl + adminId +"/"+ user.id;
    return this.http.patch(`${url}`, JSON.stringify(user), {headers});
  }

  allowUser(user: User, adminId: any) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = this.accountEnableUrl + adminId;
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
