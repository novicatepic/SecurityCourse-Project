import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
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
import { ShowForumComponent } from './show-forum/show-forum.component';
import { ManageCommentsComponent } from './manage-comments/manage-comments.component';
import { ChangeUserRoleComponent } from './change-user-role/change-user-role.component';
import { AuthService } from './interceptors/auth.service';
import { MainNavComponent } from './main-nav/main-nav.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { ChooseAccountComponent } from './choose-account/choose-account.component';
import { HomeComponent } from './home/home.component';
import { CallbackComponent } from './callback/callback.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CodeComponent,
    RegisterComponent,
    AdminManageAccountsComponent,
    AdminManageSingleAccountComponent,
    ShowForumComponent,
    ManageCommentsComponent,
    ChangeUserRoleComponent,
    MainNavComponent,
    ChooseAccountComponent,
    HomeComponent,
    CallbackComponent
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
    BrowserAnimationsModule,
    MatSidenavModule,
    MatListModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthService,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
