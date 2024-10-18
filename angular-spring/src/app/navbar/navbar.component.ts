import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../user';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  errors:any
  currentUser: User;
  subscriptions: Subscription[] = []
  constructor(private authSrv:AuthService, public router: Router, public toastr: ToastrService){ 
    this.currentUser = Object.create(null);
  }

  ngOnInit(): void {
    const subscription = this.authSrv.currentUserProvider
      .subscribe((user: User) => this.currentUser = user)
    if (!this.subscriptions.includes(subscription)) {
      this.subscriptions.push(subscription)
    }
  }

  logout(){}
}
