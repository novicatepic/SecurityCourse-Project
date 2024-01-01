import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/Comment';

@Injectable({
  providedIn: 'root'
})
export class ManageCommentsService {

  //HARD-KODOVAN admin id
  private commentDisableUrl = 'http://localhost:8080/admins/disable-comments';

  //HARD-KODOVAN admin id
  private commentEnableUrl = 'http://localhost:8080/admins/enable-comments';

  private commentsUrl = 'http://localhost:8080/admins/comments';

  constructor(private http:HttpClient) { }


  terminateComment(comment: Comment): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    /*const url = this.accountDisableUrl + user.id;*/
    return this.http.patch(`${this.commentDisableUrl}`, JSON.stringify(comment), {headers});
  }

  allowComment(comment: Comment) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    /*const url = this.accountEnableUrl + user.id;*/
    return this.http.patch(`${this.commentEnableUrl}`, JSON.stringify(comment), {headers});
  }

  getComments() : Observable<any> {
    return this.http.get(`${this.commentsUrl}`);
  }
}
