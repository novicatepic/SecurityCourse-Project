import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  public firstForm : FormGroup;
  public secondForm : FormGroup;

  correctCredentials: boolean = false;
  userId: any;

  constructor( 
    private formBuilder: FormBuilder,
     private router: Router,
     private service: LoginService,
     private jwtService: JwtTokenService,
     private snackBarService: SnackBarService, 
     private authService: AuthServiceService) {

    this.firstForm = formBuilder.group({
      username : [null, [Validators.required, Validators.maxLength(45)]],
      password : [null, [Validators.required, Validators.maxLength(500)]]
    });

    this.secondForm = formBuilder.group({
      code : [null, [Validators.required, Validators.maxLength(4), Validators.minLength(4)]]
    });

  }

  login() {

    if(this.firstForm.valid) {
      const user = {
        username: this.firstForm.get('username')?.value,
        password: this.firstForm.get('password')?.value
      }
      
      this.service.loginUserUPW(user).subscribe((data) => {
        if(data!=null && data.success) {
          this.snackBarService.triggerSnackBar("Correct credentials!");
          this.correctCredentials = true;
          this.userId = data.userId;
          //sessionStorage.setItem("usrtemp", JSON.stringify(data.userId));
          //this.router.navigate(['/code/'+data.userId]);
        } else {
          this.snackBarService.triggerSnackBar("Incorrect credentials!");
        }
      },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Incorrect credentials!");
      } );

    }
  }

  inputCode() {
    if(this.secondForm.valid) {
      const code = {
        code: this.secondForm.get('code')?.value,
        userId: parseInt(this.userId, 10)
      }

      this.service.loginUserCode(code).subscribe((data) => {
        if(data != null && data !== "null") {
          const token = JSON.stringify(data);
          //console.log("token " + token);
          localStorage.setItem("user", token);
          this.authService.notifyLoginSuccess();
          this.snackBarService.triggerSnackBar("Successfull login!");
          this.router.navigate(['/']);
        } else {
          this.snackBarService.triggerSnackBar("Code not correct!");
        }  
      },
      error => {
        //console.log(error);
        this.snackBarService.triggerSnackBar("Code not correct!");
      } );
  }
}


  private clientId = environment.clientId;
  private redirectUri = environment.redirectUri;
  loginWithGithub() {
    window.location.href=`https://github.com/login/oauth/authorize?client_id=${this.clientId}&redirect_uri=${this.redirectUri}`
  }

}
