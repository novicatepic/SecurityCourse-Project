import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/Comment';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ManageCommentsService {

  private commentDisableUrl = environment.commentDisableUrl;

  private commentsUrl = environment.commentsUrl;

  constructor(private http:HttpClient) { }


  terminateComment(comment: Comment): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    /*const url = this.accountDisableUrl + user.id;*/
    return this.http.patch(`${this.commentDisableUrl}`, JSON.stringify(comment), {headers});
  }

  

  getComments(userId: any) : Observable<any> {
    const url = this.commentsUrl + userId;
    return this.http.get(`${url}`);
  }
}
