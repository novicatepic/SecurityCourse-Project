import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../model/Comment';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentModificationService {

  private commentUrl = environment.commentUrl;
  private commentEnableUrl = environment.commentEnableUrl;

  constructor(private http:HttpClient) { }


  getComment(id: any, userId: any): Observable<any> {
    const url = this.commentUrl + id + "/" + userId;
    return this.http.get(`${url}`);
  }

  allowComment(comment : Comment) : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.patch(`${this.commentEnableUrl}`, JSON.stringify(comment), {headers});
  }
}
