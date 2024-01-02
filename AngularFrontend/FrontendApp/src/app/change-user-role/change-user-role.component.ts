import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminManageAccountsService } from '../admin-manage-accounts/admin-manage-accounts.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AdminManageSingleAccountService } from '../admin-manage-single-account/admin-manage-single-account.service';
import { User } from '../model/User';

@Component({
  selector: 'app-change-user-role',
  templateUrl: './change-user-role.component.html',
  styleUrl: './change-user-role.component.css'
})
export class ChangeUserRoleComponent {
  public secondForm: FormGroup;
  id:any;
  user?: User;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private service: AdminManageAccountsService,
    private roleService: AdminManageSingleAccountService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService,
    private route: ActivatedRoute) {
      
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id');
    });

    this.readData();


    /*console.log("User", this.user );
    console.log("ROLE", this.user?.role);*/
    this.secondForm = formBuilder.group({
      role: [null, Validators.required]
    });

  }

  readData() {

    this.service.getUserById(this.id).subscribe((data) => {
      this.user = data;
      this.secondForm.get('role')?.setValue(this.user?.role);
    },
      error => {
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });

  }

  updateRole(user: any)  {
    const roleValue = this.secondForm.get("role")?.value;
    if(this.secondForm.valid && roleValue != user.role) {
      user.role = roleValue;
      user.authorities = null;
      console.log("USER " + JSON.stringify(user));
      
      this.roleService.modifyUser(user).subscribe((data) => {
        console.log(data);
        this.snackBarService.triggerSnackBar("Role modified!");
      },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't modify role!");
      })
    }
  }

}
