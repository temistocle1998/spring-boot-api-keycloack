import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { AuthService } from './auth.service';
import { Ec2dltInterceptor } from './ec2dlt.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ListComponent } from './post/list/list.component';
import { NavbarComponent } from './navbar/navbar.component';
import { PostDetailComponent } from './post/post-detail/post-detail.component';
import { CreatePostComponent } from './post/create-post/create-post.component';

const routes: Routes = [
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'posts', component: ListComponent
  },
  {
    path: 'posts/:id/detail', component: PostDetailComponent
  },
  {
    path: 'posts/create', component: CreatePostComponent
  }
]
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ListComponent,
    NavbarComponent,
    PostDetailComponent,
    CreatePostComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(
      {
        positionClass: 'toast-top-right',
        preventDuplicates: true,
        timeOut: 3500,
        progressBar: true
      }
    ),
    RouterModule.forRoot(routes)
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: appInitFactory,
      deps: [AuthService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS, 
      useClass: Ec2dltInterceptor, 
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class AppModule { }
export function appInitFactory(authService: AuthService): () => Promise<any> {
  return () => authService.getCurrentUser();
}