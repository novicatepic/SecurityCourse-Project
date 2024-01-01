import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import { MatSnackBarModule } from '@angular/material/snack-bar';



import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CodeComponent } from './code/code.component';
import { RegisterComponent } from './register/register.component';
import { AdminManageAccountsComponent } from './admin-manage-accounts/admin-manage-accounts.component';
import { AdminManageSingleAccountComponent } from './admin-manage-single-account/admin-manage-single-account.component';
import { AdminManagePermissionsComponent } from './admin-manage-permissions/admin-manage-permissions.component';
import { ShowForumComponent } from './show-forum/show-forum.component';
import { ManageCommentsComponent } from './manage-comments/manage-comments.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CodeComponent,
    RegisterComponent,
    AdminManageAccountsComponent,
    AdminManageSingleAccountComponent,
    AdminManagePermissionsComponent,
    ShowForumComponent,
    ManageCommentsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatSnackBarModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
