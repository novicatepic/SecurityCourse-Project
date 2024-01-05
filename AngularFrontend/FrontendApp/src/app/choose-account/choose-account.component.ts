import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ChooseAccountService } from './choose-account.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { User } from '../model/User';

@Component({
  selector: 'app-choose-account',
  templateUrl: './choose-account.component.html',
  styleUrl: './choose-account.component.css'
})
export class ChooseAccountComponent {
  users : Array<User> = new Array();
  toAllow?: Comment;
  userId: any;
  public firstForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private service: ChooseAccountService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService) {
      var temp = this.jwtService.extractTokenInfo();
      this.userId = temp.id;
      this.readData();

      this.firstForm = formBuilder.group({
        title: [null, [Validators.required, Validators.maxLength(45)]],
        content: [null, [Validators.required, Validators.maxLength(2000)]]
      });

  }

  readData() {
    this.service.getUsers().subscribe((data) => {
      console.log(JSON.stringify(data));
      this.users = data;
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }
}
