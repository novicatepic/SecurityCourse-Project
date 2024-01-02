import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JwtTokenService } from '../jwt-token/jwt-token.service'; // Replace with the actual path to your JwtTokenService

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {

  constructor(private jwtService: JwtTokenService, private router: Router) { }

  canActivate(): boolean {
    const token = this.jwtService.extractToken();
    const tokenInfo = this.jwtService.extractTokenInfo();
    /*console.log("token = " + JSON.stringify(token));
    console.log("Expired " + this.jwtService.checkIfTokenExpired(token));
    console.log("role " + token.role);*/
    if (token && !this.jwtService.checkIfTokenExpired(token) && tokenInfo.role === "ROLE_ADMIN") {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}