import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminManageAccountsService } from '../admin-manage-accounts/admin-manage-accounts.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { User } from '../model/User';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Room } from '../model/Room';
import { UserPermissionsRoom } from '../model/UserPermissionsRoom';
import { AdminManageSingleAccountService } from './admin-manage-single-account.service';

@Component({
  selector: 'app-admin-manage-single-account',
  templateUrl: './admin-manage-single-account.component.html',
  styleUrl: './admin-manage-single-account.component.css'
})
export class AdminManageSingleAccountComponent {
  public firstForm: FormGroup;
  public secondForm: FormGroup;
  rooms: Array<Room> = new Array();
  user: any;
  id: any;

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

    this.firstForm = formBuilder.group({
      room: [null, Validators.required],
      create: [false, Validators.required],
      update: [false, Validators.required],
      delete: [false, Validators.required],
      role: [null, Validators.required]
    });

    this.secondForm = formBuilder.group({
      role: [null, Validators.required]
    });

    this.readData();

  }

  readData() {

    this.service.getUserById(this.id).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.user = data;
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });

    this.service.getRooms().subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.rooms = data;
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }

  updateRole(user: any)  {
    if(this.secondForm.valid) {
      user.role = this.secondForm.get("role")?.value;
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

  updatePermissions() {
    if (this.firstForm.valid) {
      const roomId = parseInt(this.firstForm.get("room")?.value, 10);
      const createPermission = this.firstForm.get("create")?.value;
      const updatePermission = this.firstForm.get("update")?.value;
      const deletePermission = this.firstForm.get("delete")?.value;

      console.log(this.id);
      console.log(roomId);
      console.log(createPermission);
      console.log(updatePermission);
      console.log(deletePermission);

      // Assuming UserPermissionsRoom has appropriate constructor parameters
      const permissions = new UserPermissionsRoom(this.id, roomId, createPermission, updatePermission, deletePermission);
      
      //console.log("perm " + JSON.stringify(permissions));

      this.service.addPermissions(permissions).subscribe((data) => {
          console.log(JSON.stringify(data));
          this.snackBarService.triggerSnackBar("Permission added!");
      },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't add permissions!");
      })
    
    }
  }
}
