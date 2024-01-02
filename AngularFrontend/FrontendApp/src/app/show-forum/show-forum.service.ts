import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShowForumService {

  private commentsUrl = 'http://localhost:8080/rooms/comments/';

  private roomUrl = 'http://localhost:8080/rooms/';

  private permissionsUrl = 'http://localhost:8080/permissions/';
  
  constructor(private http:HttpClient) { }

  loadComments(roomId: any): Observable<any> {
    const url = this.commentsUrl + roomId;
    return this.http.get(`${url}`);
  }

  loadRoomInfo(roomId: any): Observable<any> {
    const url = this.roomUrl + roomId;
    return this.http.get(`${url}`);
  }

  loadPermissionsForForum(roomId: any, userId: any): Observable<any> {
    const url = this.permissionsUrl + roomId + "/" + userId;
    return this.http.get(`${url}`);
  }

}
