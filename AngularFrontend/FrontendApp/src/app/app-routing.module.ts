import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CodeComponent } from './code/code.component';
import { RegisterComponent } from './register/register.component';
import { AdminManageAccountsComponent } from './admin-manage-accounts/admin-manage-accounts.component';
import { AdminManageSingleAccountComponent } from './admin-manage-single-account/admin-manage-single-account.component';
import { ShowForumComponent } from './show-forum/show-forum.component';
import { ManageCommentsComponent } from './manage-comments/manage-comments.component';
import { ChangeUserRoleComponent } from './change-user-role/change-user-role.component';
import { AdminGuard } from './guards/admin.guard';
import { ModeratorGuard } from './guards/moderator.guard';
import { ForumUserGuard } from './guards/form-user.guard';
import { UrlGuard } from './guards/url.guard';

const routes: Routes = [
  {path: 'login', component: LoginComponent}, //OK
  {path: 'code/:id', component: CodeComponent}, //OK 
  {path: 'register', component: RegisterComponent}, //OK
  {path: 'manage-accounts', component: AdminManageAccountsComponent, canActivate: [AdminGuard]}, //OK
  {path: 'manage-comments', component: ManageCommentsComponent, canActivate: [ModeratorGuard]}, //OK
  {path: 'manage-account/:id', component: AdminManageSingleAccountComponent, canActivate: [AdminGuard, UrlGuard]},
  //{path: 'manage-permissions/:id', component: ChangeUserRoleComponent, canActivate: [AdminGuard, UrlGuard]},
  {path: 'room/:id', component: ShowForumComponent, canActivate: [ForumUserGuard]} //OK
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
