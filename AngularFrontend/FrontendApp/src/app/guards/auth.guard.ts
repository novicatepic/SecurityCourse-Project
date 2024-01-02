import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JwtTokenService } from '../jwt-token/jwt-token.service'; // Replace with the actual path to your JwtTokenService

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(private jwtService: JwtTokenService, private router: Router) { }

  canActivate(): boolean {
    const token = this.jwtService.extractToken();
    console.log("token = " + JSON.stringify(token));
    if (token && !this.jwtService.checkIfTokenExpired(token)) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
