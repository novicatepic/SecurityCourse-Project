import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JwtTokenService } from '../jwt-token/jwt-token.service'; // Replace with the actual path to your JwtTokenService

@Injectable({
  providedIn: 'root'
})
export class ForumUserGuard {

  constructor(private jwtService: JwtTokenService, private router: Router) { }

  canActivate(): boolean {
    const token = this.jwtService.extractToken();
    const tokenInfo = this.jwtService.extractTokenInfo();
    if (token && !this.jwtService.checkIfTokenExpired(token) && tokenInfo.role === "ROLE_FORUM") {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}