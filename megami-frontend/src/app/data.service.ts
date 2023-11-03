import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Response } from 'src/models/Response';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(
    private http: HttpClient
  ) { }

  getAllPosts() {
    return this.http.get<Response>(
      'http://localhost:8080/posts/all'
    )
  }

  getFeedPosts() {
    return this.http.get<Response>(
      'http://localhost:8080/posts/feed'
    )
  }

  getAllPostsFromUser(username: string) {
    return this.http.get<Response>(
      `http://localhost:8080/posts/from/${username}`
    )
  }

  getPost(filename: string) {
    return this.http.get<Response>(
      `http://localhost:8080/posts/${filename}`
    )
  }

  uploadPost(formData: FormData) {
    return this.http.post<Response>(
      'http://localhost:8080/posts/add',
      formData
    );
  }

  updatePost(filename: string, formData: FormData) {
    return this.http.patch<Response>(
      `http://localhost:8080/posts/${filename}`,
      formData
    );
  }

  deletePost(filename: string) {
    return this.http.delete<Response>(
      `http://localhost:8080/posts/${filename}`
    );
  }

  getUser(username: string) {
    return this.http.get<Response>(
      `http://localhost:8080/users/${username}`
    )
  }

  followUser(username: string) {
    return this.http.post<Response>(
      `http://localhost:8080/users/${username}/follow`,
      {}
    )
  }
}
