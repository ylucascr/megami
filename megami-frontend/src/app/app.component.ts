import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  loggedInUser: Observable<String | null> = of(null);
  showHeader = false;
  
  constructor(
    public router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe(() => {
      this.loggedInUser = this.authService.loggedInUser.asObservable();
    });
  }

  handleLogout(e: Event) {
    e.preventDefault();
    this.authService.logout();
  }

  handleDelete(e: Event) {
    e.preventDefault();
    this.authService.deleteAccount().subscribe(() => this.router.navigate(['/auth']));
  }

  activate(e: Event) {
    console.log(e);
  }
}
