import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JwtTokenService } from '../jwt-token/jwt-token.service'; // Replace with the actual path to your JwtTokenService
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class ForumUserGuard {

  constructor(private jwtService: JwtTokenService, private router: Router, private snackService: SnackBarService, 
    private authService: AuthServiceService) { }

  canActivate(): boolean {
    const token = this.jwtService.extractToken();
    const tokenInfo = this.jwtService.extractTokenInfo();
    if (token && !this.jwtService.checkIfTokenExpired(token) && tokenInfo.role !== "ROLE_UNDEFINED") {
      return true;
    } else {
      localStorage.removeItem("user");
      this.snackService.triggerSnackBar("Malicious activity, session invalidated");
      this.authService.notifyTermination();
      setTimeout(()=>{}, 2000);
      
      return false;
    }
  }
}