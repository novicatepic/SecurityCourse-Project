import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class JwtTokenService {

  private baseUrl = 'http://localhost:8080/users/';

  constructor(private http: HttpClient) { }

  extractToken() {
    var tokenTemp = localStorage.getItem("user");
    if(tokenTemp) {
      var token = JSON.parse(tokenTemp);
      return token.token;
    }
    console.log("NULL");
    return null;
  }

  extractTokenInfo() {
    var token = this.extractToken();
    if(token) {
      const decodedToken: any = jwtDecode(token);
      console.log("decoded " + JSON.stringify(decodedToken));
      return decodedToken;
    }
    return null;
  }

  getUserById() : any {
    var tokenInfo = this.extractTokenInfo();
    if(tokenInfo) {
      const url = this.baseUrl + tokenInfo.id;
      return this.http.get<any[]>(`${url}`);
    }
    console.log("NULL");
    return null;
  }

  checkIfTokenExpired(token: any) {
    const decodedToken = this.extractTokenInfo();
    const expirationDate = decodedToken.exp * 1000; // Convert to milliseconds
    return expirationDate < Date.now();
  }
}
