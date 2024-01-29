import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class CodeGuard {

  constructor(private router: Router, private snackService: SnackBarService, 
    private authService: AuthServiceService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const usrTemp = sessionStorage.getItem("usrtemp");
    const userId = route.params['id'];
    if(usrTemp) {
        const parsed = JSON.parse(usrTemp);
        if(userId == parsed) {
            sessionStorage.removeItem("usrtemp");
            return true;
        }
        localStorage.removeItem("user");
      this.snackService.triggerSnackBar("Malicious activity, session invalidated");
      this.authService.notifyTermination();
      setTimeout(()=>{}, 2000);
      
      return false;
    } else {
      localStorage.removeItem("user");
      this.snackService.triggerSnackBar("Malicious activity, session invalidated");
      this.authService.notifyTermination();
      setTimeout(()=>{}, 2000);
      
        return false;
    }
  }
}