import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../login/login.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';

@Component({
  selector: 'app-code',
  templateUrl: './code.component.html',
  styleUrl: './code.component.css'
})
export class CodeComponent {
  public firstForm : FormGroup
  question: any;
  id:any;

  constructor(
    private formBuilder: FormBuilder,
     private router: Router, 
     private service : LoginService,
     private route: ActivatedRoute,
     private snackBarService: SnackBarService) 
     
     {
    this.firstForm = formBuilder.group({
      code : [null, [Validators.required, Validators.maxLength(4), Validators.minLength(4)]]
    });
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id');
    });
   
  }

  inputCode() {
    if(this.firstForm.valid) {
      const code = {
        code: this.firstForm.get('code')?.value,
        userId: parseInt(this.id, 10)
      }

      this.service.loginUserCode(code).subscribe((data) => {
        const token = JSON.stringify(data);
        localStorage.setItem("user", token);
        this.snackBarService.triggerSnackBar("Successfull login!");
        //this.router.navigate(['/']);
      },
      error => {
        console.log(error);
        this.snackBarService.triggerSnackBar("Code not correct!");
      } );
  }
}
}
