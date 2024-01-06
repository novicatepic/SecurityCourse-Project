import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CallbackService } from './callback.service';
import { AuthService } from '../interceptors/auth.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.css'
})
export class CallbackComponent {

  userId = "";
  user: any;

  constructor(private route: ActivatedRoute, private userService: CallbackService, private authService: AuthServiceService,
    private snackBarService: SnackBarService, private router: Router) {

    const code = this.route.snapshot.queryParamMap.get('code');
    //console.log("Code = " + code);

    if(code != null) {
      this.userService.getUserID(code).subscribe((data)=>{
        //console.log(JSON.stringify(data));
        const token = JSON.stringify(data);
        localStorage.setItem("user", token);
          this.authService.notifyLoginSuccess();
          this.snackBarService.triggerSnackBar("Successfull login!");
          this.router.navigate(['/']);
       },(error)=>{
        this.snackBarService.triggerSnackBar("Bad login!");
         this.userId =error.error.text;
       })
    }
    
  }
}
