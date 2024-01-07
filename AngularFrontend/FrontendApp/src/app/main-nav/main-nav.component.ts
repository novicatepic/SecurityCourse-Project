import { Component, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { Router } from '@angular/router';
import { AuthServiceService } from '../auth-service/auth-service.service';
import { LogoutServiceService } from './logout-service.service';

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent {
  private breakpointObserver = inject(BreakpointObserver);

  user: any;
  loggedIn: boolean = false;

  constructor(private jwtService: JwtTokenService, 
    private router : Router, 
    private authService2 : AuthServiceService,
    private logoutService: LogoutServiceService) {

    //user logged in
    if(this.jwtService.getUserById()) {
      this.jwtService.getUserById().subscribe((data: any) => {
        this.loggedIn = true;
        this.user = data;
     })} 
    else {
      this.loggedIn = false;
    }

  }

  ngOnInit() {
    this.authService2.loginSuccess$.subscribe(() => {
      this.loggedIn = true;
      this.jwtService.getUserById().subscribe((data: any) => {
        this.loggedIn = true;
        this.user = data;
     }
     );
    });
  }

  public goToPage(value: any) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
        return false;
    }
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['/room', value]);
}

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

    logout() {
      localStorage.removeItem("user");
      this.loggedIn = false;
      this.logoutService.logoutUser().subscribe(() => {
        //console.log("Blacklisted!");
      })
      this.router.navigate(['/login']);
    }
}
