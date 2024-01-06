import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CallbackService {

  baseUrl = environment.base8080Url;

  constructor(private httpClient: HttpClient) { }


  public getUserID(pid:any){
    return this.httpClient.get(this.baseUrl + "/api/callback?code="+ pid)
  }

  public addUser(user: any){
    return this.httpClient.post(this.baseUrl + "/user/registerNewUser", JSON.stringify(user));
  }

}
