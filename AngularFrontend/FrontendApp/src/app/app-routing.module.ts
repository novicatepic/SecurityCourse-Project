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
import { CommentModificationComponent } from './comment-modification/comment-modification.component';
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { ModeratorGuard } from './guards/moderator.guard';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'code/:id', component: CodeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'manage-accounts', component: AdminManageAccountsComponent, canActivate: [AdminGuard]},
  {path: 'manage-comments', component: ManageCommentsComponent, canActivate: [AdminGuard, ModeratorGuard]},
  {path: 'manage-comment/:commentId', component: CommentModificationComponent, canActivate: [AdminGuard, ModeratorGuard]},
  {path: 'manage-account/:id', component: AdminManageSingleAccountComponent, canActivate: [AdminGuard]},
  {path: 'manage-permissions/:id', component: ChangeUserRoleComponent, canActivate: [AdminGuard]},
  {path: 'room/:id', component: ShowForumComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
