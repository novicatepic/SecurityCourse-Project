import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CodeComponent } from './code/code.component';
import { RegisterComponent } from './register/register.component';
import { AdminManageAccountsComponent } from './admin-manage-accounts/admin-manage-accounts.component';
import { AdminManageSingleAccountComponent } from './admin-manage-single-account/admin-manage-single-account.component';
import { AdminManagePermissionsComponent } from './admin-manage-permissions/admin-manage-permissions.component';
import { ShowForumComponent } from './show-forum/show-forum.component';
import { ManageCommentsComponent } from './manage-comments/manage-comments.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'code/:id', component: CodeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'manage-accounts', component: AdminManageAccountsComponent},
  {path: 'manage-comments', component: ManageCommentsComponent},
  {path: 'manage-account/:id', component: AdminManageSingleAccountComponent},
  {path: 'manage-permissions/:id', component: AdminManagePermissionsComponent},
  {path: 'room/:id', component: ShowForumComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
