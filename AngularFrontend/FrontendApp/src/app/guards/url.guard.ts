import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';

@Injectable({
  providedIn: 'root',
})
export class UrlGuard {


  constructor(private router: Router, private jwtService: JwtTokenService, private snackService: SnackBarService, 
    private authService: AuthServiceService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    const id = route.paramMap.get('id');
    this.jwtService.getUserById2(id).subscribe((user: any) => {
        if(user.role == "ROLE_ADMIN") {
          localStorage.removeItem("user");
          this.snackService.triggerSnackBar("Malicious activity, session invalidated");
          this.authService.notifyTermination();
          setTimeout(()=>{}, 2000);
          return false;
        }
        return true;
    })



    const token = this.jwtService.extractToken();
    const tokenInfo = this.jwtService.extractTokenInfo();
    // Assuming AuthService has a method like isAdminWithId
    if (token && !this.jwtService.checkIfTokenExpired(token) && tokenInfo.id != id) {
      return true;
    } else {
      // Redirect to a forbidden or unauthorized page
      localStorage.removeItem("user");
      this.snackService.triggerSnackBar("Malicious activity, session invalidated");
      this.authService.notifyTermination();
      setTimeout(()=>{}, 2000);
      
      return false;
    }
    
  }
}
