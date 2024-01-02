import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/Comment';

@Injectable({
  providedIn: 'root'
})
export class CommentModificationService {

  private commentUrl = 'http://localhost:8080/comments/';
  private commentEnableUrl = 'http://localhost:8080/admins/enable-comments';

  constructor(private http:HttpClient) { }


  getComment(id: any): Observable<any> {
    const url = this.commentUrl + id;
    return this.http.get(`${url}`);
  }

  allowComment(comment : Comment) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.patch(`${this.commentEnableUrl}`, JSON.stringify(comment), {headers});
  }
}
