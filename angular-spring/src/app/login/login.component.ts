import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { PromiseError } from '../promise-error';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userLogin = { username: '', password: '' }
  error: any

  constructor(private authService: AuthService, public router: Router) { }

  onSubmit() {
    return this.authService.login(this.userLogin).then((data:any) => {
      this.authService.setToken(data?.token, data?.expire_token);
        this.router.navigate(['/']);
    });
  }
}
