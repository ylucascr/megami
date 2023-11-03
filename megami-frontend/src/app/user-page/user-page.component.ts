import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, map, of, switchMap } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss']
})
export class UserPageComponent {
  user: any = {};
  posts: any[] = [];
  isFetching = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private dataService: DataService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      map(params => params.get('username')),
      switchMap(username => {
        if (username === 'unknown') return of({ data: { username } });
        return this.dataService.getUser(username!)
      }),
      catchError(() => of(null)),
      map(res => {
        if (!res) return res;
        return res.data;
      })
    ).subscribe(user => {
      if (!user) this.router.navigate(['/']);
      this.user = user;
      this.getPosts(user);
    })
  }

  getPosts(user: any) {
    this.dataService.getAllPostsFromUser(user.username).subscribe(res => {
      this.posts = res.data;
      this.isFetching = false;
    });
  }

  followUser(e: Event) {
    e.preventDefault();

    this.dataService.followUser(this.user.username).pipe(
      switchMap(() => this.dataService.getUser(this.user.username)),
      map(res => res.data)
    ).subscribe(user => this.user = user);
  }
}
