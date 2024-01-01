import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShowForumService {

  private baseUrl = 'http://localhost:8080/rooms/comments/';
  
  constructor(private http:HttpClient) { }

  loadData(roomId: any): Observable<any> {
    const url = this.baseUrl + roomId;
    return this.http.get(`${url}`);
  }
}
