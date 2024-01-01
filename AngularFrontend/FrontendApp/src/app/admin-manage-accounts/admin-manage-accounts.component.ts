import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
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

  constructor(
    private router: Router,
    private service: AdminManageAccountsService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService) {

      this.readData();

  }

  readData() {

    //HARD-KODOVANO 1 ISPOD
    this.service.getWaitingRequests(1).subscribe((data) => {
      console.log(JSON.stringify(data));
      this.users = data;
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }

  terminateRequest(user: User) {

    user.active = false;
    user.isTerminated = true;

    this.service.terminateUser(user).subscribe((data) => {
      console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("User terminated!");
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error!");
      })

  }

  acceptRequest(user: User) {

    user.active = true;
    user.isTerminated = false;

    this.service.allowUser(user).subscribe((data) => {
      console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("User activated!");
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error!");
      })

  }
}