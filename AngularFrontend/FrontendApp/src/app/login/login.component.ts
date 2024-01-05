import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthServiceService } from '../auth-service/auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  public firstForm : FormGroup

  constructor( 
    private formBuilder: FormBuilder,
     private router: Router,
     private service: LoginService,
     private jwtService: JwtTokenService,
     private snackBarService: SnackBarService) {

    this.firstForm = formBuilder.group({
      username : [null, [Validators.required, Validators.maxLength(45)]],
      password : [null, [Validators.required, Validators.maxLength(500)]]
    });
  }

  login() {

    if(this.firstForm.valid) {
      const user = {
        username: this.firstForm.get('username')?.value,
        password: this.firstForm.get('password')?.value
      }



      this.service.loginUserUPW(user).subscribe((data) => {
        console.log(JSON.stringify(data));
        if(data.success) {
          this.snackBarService.triggerSnackBar("Correct credentials!");
          this.router.navigate(['/code/'+data.userId]);
        } else {
          this.snackBarService.triggerSnackBar("Incorrect credentials!");
        }
      },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Incorrect credentials!");
      } );

    }
  }
}
