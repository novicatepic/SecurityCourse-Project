import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AdminManageAccountsComponent } from './admin-manage-accounts/admin-manage-accounts.component';
import { AdminManageSingleAccountComponent } from './admin-manage-single-account/admin-manage-single-account.component';
import { ShowForumComponent } from './show-forum/show-forum.component';
import { ManageCommentsComponent } from './manage-comments/manage-comments.component';
import { AdminGuard } from './guards/admin.guard';
import { ModeratorGuard } from './guards/moderator.guard';
import { ForumUserGuard } from './guards/form-user.guard';
import { UrlGuard } from './guards/url.guard';
import { ChooseAccountComponent } from './choose-account/choose-account.component';
import { HomeComponent } from './home/home.component';
import { CallbackComponent } from './callback/callback.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent}, //OK
  {path: 'callback', component: CallbackComponent}, 
  {path: 'manage-accounts', component: AdminManageAccountsComponent, canActivate: [AdminGuard]}, //OK
  {path: 'manage-comments', component: ManageCommentsComponent, canActivate: [ModeratorGuard]}, //OK
  {path: 'manage-account-permissions', component: ChooseAccountComponent, canActivate: [AdminGuard]},
  {path: 'manage-account/:id', component: AdminManageSingleAccountComponent, canActivate: [AdminGuard, UrlGuard]},
  {path: 'room/:id', component: ShowForumComponent, canActivate: [ForumUserGuard]}, //OK
  {path: '', component: HomeComponent}, //OK
  { path: '**', redirectTo: '/', pathMatch: 'full' }, //OK
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
