import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthFormPageComponent } from './auth-form-page/auth-form-page.component';
import { MainPageComponent } from './main-page/main-page.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { authGuard, loggedInRedirect } from './auth/auth.guard';
import { UploadFormPageComponent } from './upload-form-page/upload-form-page.component';
import { UserPageComponent } from './user-page/user-page.component';

const routes: Routes = [
  { path: 'auth', component: AuthFormPageComponent, canActivate: [loggedInRedirect] },
  { path: 'form', component: UploadFormPageComponent, canActivate: [authGuard] },
  { path: ':filename/edit', component: UploadFormPageComponent, canActivate: [authGuard] },
  { path: 'u/:username' , component: UserPageComponent },
  { path: 'feed' , component: MainPageComponent },
  { path: '' , component: MainPageComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
