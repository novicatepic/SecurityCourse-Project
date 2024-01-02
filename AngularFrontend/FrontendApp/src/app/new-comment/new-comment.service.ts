import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/Comment';

@Injectable({
  providedIn: 'root'
})
export class NewCommentService {

  private commentUrl = 'http://localhost:8080/comments';

  constructor(private http:HttpClient) { }


  saveComment(comment: Comment): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.commentUrl}`, JSON.stringify(comment), {headers});
  }

  deleteComment(id: any) : Observable<any> {
    const url = this.commentUrl + "/" + id;
    return this.http.delete(`${url}`);
  }

  updateComment(comment: Comment) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.patch(`${this.commentUrl}`, JSON.stringify(comment), {headers});
  }

}
