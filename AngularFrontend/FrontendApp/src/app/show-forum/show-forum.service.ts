import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ShowForumService {

  private commentsUrl = environment.commentsForumUrl;

  private roomUrl = environment.roomForumUrl;

  private permissionsUrl = environment.permissionsForumUrl;
  
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
