import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminManageAccountsService } from './admin-manage-accounts.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { User } from '../model/User';

@Component({
  selector: 'app-admin-manage-accounts',
  templateUrl: './admin-manage-accounts.component.html',
  styleUrl: './admin-manage-accounts.component.css'
})
export class AdminManageAccountsComponent {

  users : any = [];
  userId: any;
  public firstForm: FormGroup;

  constructor(
    private router: Router,
    private service: AdminManageAccountsService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService,
    private formBuilder: FormBuilder) {

      var temp = this.jwtService.extractTokenInfo();
      this.userId = temp.id;

      this.firstForm = formBuilder.group({
        role: [null, [Validators.required, Validators.maxLength(45)]]
      });

      this.readData();

  }

  readData() {

    //HARD-KODOVANO 1 ISPOD
    this.service.getWaitingRequests(this.userId).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.users = data;
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }

  terminateRequest(user2: User) {

    user2.active = false;
    user2.isTerminated = true;

    this.service.terminateUser(user2, this.userId).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("User terminated!");
      location.reload();
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error, your token is possibly not valid anymore!");
      })

  }

  acceptRequest(user2: any) {
    if(this.firstForm.valid) {
      user2.active = true;
      user2.isTerminated = false;
      user2.role = this.firstForm.get("role")?.value;
      user2.authorities = null;
    this.service.allowUser(user2, this.userId).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("User activated!");
      location.reload();
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error, your token is possibly not valid anymore!");
      })
    }
    

  }
}