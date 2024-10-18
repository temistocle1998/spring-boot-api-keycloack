import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Ec2dltHttpService } from './ec2dlt-http-service';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenName = 'Bearer';
  private token: string | null = null;
  private currentUserManager: BehaviorSubject<any> = new BehaviorSubject(null);
  public currentUserProvider = this.currentUserManager.asObservable();

  constructor(public http: Ec2dltHttpService) { }

  public get currentUserValue(): User {
    return this.currentUserManager.value;
  }

  public getTokenName() {
    return this.tokenName;
  }

  public getToken() {
    if (this.token == null) {
      return this.token = localStorage.getItem(this.tokenName);
    }
    return this.token;
  }

  public removeAllConnexionData() {
    this.token == null
    localStorage.removeItem(this.tokenName);
    this.currentUserManager.next(null);
  }

  public setToken(token: string, expireToken: string) {
    this.token = token;
    const tokenLifetime = 1800; // 30 minutes in seconds
const expiryTimestamp = Math.floor(Date.now() / 1000) + tokenLifetime;

    localStorage.setItem(this.tokenName, token);
    localStorage.setItem('expire_token', `${expiryTimestamp}`); // Store the exact expiry timestamp.
  }

  setCurrentUser(user: any) {
    this.currentUserManager.next(user);
  }

  getCurrentUser() {
    return new Promise((resolve) => {
      this.http.get('current-user')
        .then((user: any) => {
          this.setCurrentUser(user);
          resolve(user);
        }).catch(() => {
          resolve(null);
        });
    });
  }

  login(loginData: { username: string, password: string }) {
    return this.http.post('login', loginData);
  }

  logout() {
    return this.http.post('logout', {});
  }


  getTokenExpiry(): number {
    const expiry = localStorage.getItem('expire_token'); // Store expiry time in seconds.
    return expiry ? parseInt(expiry, 10) : 0;
  }

  isTokenValid(): boolean {
    const expiryTime = this.getTokenExpiry();
    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds.
    console.log(currentTime)
    console.log(expiryTime)
    return currentTime < expiryTime;
  }
}
