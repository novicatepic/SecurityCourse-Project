import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterService } from './register.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { User } from '../model/User';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  public firstForm : FormGroup
  question: any;

  constructor( 
    private formBuilder: FormBuilder,
     private router: Router,
     private service: RegisterService,
     private snackService: SnackBarService) {

    this.firstForm = formBuilder.group({
      username : [null, [Validators.required, Validators.maxLength(45)]],
      password : [null, [Validators.required, Validators.maxLength(500)]],
      email : [null, [Validators.required, Validators.email, Validators.maxLength(200)]],
    });
  }

  registerUser() {

    if(this.firstForm.valid) {
      const user = new User(this.firstForm.get('username')?.value, 
      this.firstForm.get('password')?.value ,
      this.firstForm.get('email')?.value);


      /*console.log(user);
      console.log(JSON.stringify(user));*/

      this.service.registerUser(user).subscribe((data) => {
        const user = JSON.stringify(data);
        //console.log("user " + user);
        this.snackService.triggerSnackBar("Registration sent, waiting for admin to approve!");
        this.router.navigate(['/']);
      },
      error => {
        //console.log(error);
        this.snackService.triggerSnackBar("Error creating user!");
      } );

    }
  }
}
