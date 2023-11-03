import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html'
})
export class MainPageComponent implements OnInit {
  posts: any[] = [];
  isFetching = true;

  isFeedPage = false;

  constructor(
    private router: Router,
    private dataService: DataService,
    public authService: AuthService
  ) {
    this.isFeedPage = this.router.url.includes('feed');
  }

  ngOnInit(): void {
    this.getPosts();
  }

  getPosts() {
    (this.isFeedPage ?
      this.dataService.getFeedPosts() :
      this.dataService.getAllPosts()
    ).subscribe(res => {
      this.posts = res.data;
      this.isFetching = false;
    });
  }
}
