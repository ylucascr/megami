import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Response } from 'src/models/Response';

import { environment } from 'environments/environments';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  apiUrl = environment?.apiUrl || 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  getAllPosts() {
    return this.http.get<Response>(
      `${this.apiUrl}/posts/all`
    )
  }

  getFeedPosts() {
    return this.http.get<Response>(
      `${this.apiUrl}/posts/feed`
    )
  }

  getAllPostsFromUser(username: string) {
    return this.http.get<Response>(
      `${this.apiUrl}/posts/from/${username}`
    )
  }

  getPost(filename: string) {
    return this.http.get<Response>(
      `${this.apiUrl}/posts/${filename}`
    )
  }

  uploadPost(formData: FormData) {
    return this.http.post<Response>(
      `${this.apiUrl}/posts/add`,
      formData
    );
  }

  updatePost(filename: string, formData: FormData) {
    return this.http.patch<Response>(
      `${this.apiUrl}/posts/${filename}`,
      formData
    );
  }

  deletePost(filename: string) {
    return this.http.delete<Response>(
      `${this.apiUrl}/posts/${filename}`
    );
  }

  getUser(username: string) {
    return this.http.get<Response>(
      `${this.apiUrl}/users/${username}`
    )
  }

  followUser(username: string) {
    return this.http.post<Response>(
      `${this.apiUrl}/users/${username}/follow`,
      {}
    )
  }
}
