import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthFormPageComponent } from './auth-form-page/auth-form-page.component';
import { AuthInterceptor } from './auth/auth.interceptor';
import { MainPageComponent } from './main-page/main-page.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UploadFormPageComponent } from './upload-form-page/upload-form-page.component';
import { PostComponent } from './post/post.component';
import { UserPageComponent } from './user-page/user-page.component';
import { DATE_PIPE_DEFAULT_OPTIONS } from '@angular/common';
import { PostListComponent } from './post-list/post-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthFormPageComponent,
    MainPageComponent,
    PageNotFoundComponent,
    UploadFormPageComponent,
    PostComponent,
    UserPageComponent,
    PostListComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true   
    },
    { provide: DATE_PIPE_DEFAULT_OPTIONS, useValue: { dateFormat: 'MMM d, y HH:mm' } }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
