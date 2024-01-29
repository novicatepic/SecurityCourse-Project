import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminManageAccountsService } from '../admin-manage-accounts/admin-manage-accounts.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { User } from '../model/User';
import { ChangeDetectorRef } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  
  public thirdForm: FormGroup;
  permittedRooms: Array<UserPermissionsRoom> = new Array();
  unpermittedRooms: Array<Room> = new Array();

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
    this.readData();

    this.firstForm = formBuilder.group({
      room: [null, Validators.required],
      create: [false, Validators.required],
      update: [false, Validators.required],
      delete: [false, Validators.required]
    });

    this.thirdForm = formBuilder.group({
      newRoom: [null, Validators.required],
      createPermitted: [false, Validators.required],
      updatePermitted: [false, Validators.required],
      deletePermitted: [false, Validators.required]
    });

  }

  readData() {

    this.service.getUserById(this.id).subscribe((data) => {
      this.user = data;
    },
      error => {
        this.snackBarService.triggerSnackBar("404!");
        this.router.navigate(['/login']);
      });



      this.roleService.getPermittedRooms(this.id).subscribe((data) => {
        this.permittedRooms = data;
      },
      (error) => {
        if (error.status === 403) {
          // Handle forbidden (HTTP 403) error
          this.snackBarService.triggerSnackBar("Why would you want to change your own permissions?");
        } else if (error.status === 404) {
          this.snackBarService.triggerSnackBar("404 Not Found");
        } else {
          // Handle other errors
          //console.error("Unexpected error:", error);
          this.snackBarService.triggerSnackBar("Error");
        }
      }
    );



      this.roleService.getNotPermittedRooms(this.id).subscribe((data) => {
        this.unpermittedRooms = data;
      },
      (error) => {
        if (error.status === 403) {
          // Handle forbidden (HTTP 403) error
          this.snackBarService.triggerSnackBar("Why would you want to change your own permissions?");
        } else if (error.status === 404) {
          this.snackBarService.triggerSnackBar("404 Not Found");
        } else {
          // Handle other errors
          //console.error("Unexpected error:", error);
          this.snackBarService.triggerSnackBar("Error");
        }
      }
    );

  }

  changeCheckboxes() {
    const selectedRoomId = this.thirdForm.get('newRoom')?.value;

    //console.log("sel room " + selectedRoomId);

    var selectedRoom = this.permittedRooms.filter((room) => {
      return room.roomId == selectedRoomId;
    });

    if (selectedRoom) {
      this.thirdForm.get('createPermitted')?.setValue(selectedRoom[0].canCreate);
      this.thirdForm.get('updatePermitted')?.setValue(selectedRoom[0].canUpdate);
      this.thirdForm.get('deletePermitted')?.setValue(selectedRoom[0].canDelete);
    }

  }

  updatePermissionsPermitted() {
    if(this.thirdForm.valid) {

      const roomId = parseInt(this.thirdForm.get("newRoom")?.value, 10) ;
      const createPermission = this.thirdForm.get("createPermitted")?.value;
      const updatePermission = this.thirdForm.get("updatePermitted")?.value;
      const deletePermission = this.thirdForm.get("deletePermitted")?.value;

      const permissions = new UserPermissionsRoom(this.id, roomId, createPermission, updatePermission, deletePermission);

      this.service.addPermissions(permissions).subscribe((data) => {
        //console.log(JSON.stringify(data));
        this.snackBarService.triggerSnackBar("Permission added!");
        this.router.navigate(['/manage-account-permissions']);
    },
    error => {
      //console.log("ERROR " + JSON.stringify(error));
      this.snackBarService.triggerSnackBar("Couldn't add permissions!");
    })
    }
  }

  updatePermissionsUnpermitted() {
    if (this.firstForm.valid) {
      const roomId = parseInt(this.firstForm.get("room")?.value, 10);
      const createPermission = this.firstForm.get("create")?.value;
      const updatePermission = this.firstForm.get("update")?.value;
      const deletePermission = this.firstForm.get("delete")?.value;

      const permissions = new UserPermissionsRoom(this.id, roomId, createPermission, updatePermission, deletePermission);

      this.service.addPermissions(permissions).subscribe((data) => {
          this.snackBarService.triggerSnackBar("Permission added!");
          this.router.navigate(['/manage-account-permissions']);
      },
      error => {
        this.snackBarService.triggerSnackBar("Couldn't add permissions, your token is possibly not valid anymore!");
      })
    
    }
  }
}
