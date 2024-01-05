import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private loginSuccessSubject = new Subject<void>();

  // Observable that other components can subscribe to for login success notification
  loginSuccess$: Observable<void> = this.loginSuccessSubject.asObservable();

  // Method to notify subscribers about successful login
  notifyLoginSuccess() {
    this.loginSuccessSubject.next();
  }
}
