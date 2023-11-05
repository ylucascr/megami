import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { DataService } from '../data.service';
import { environment } from 'environments/environments';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {
  @Input({ required: true }) post: any;
  @Output() postDeleted: EventEmitter<any> = new EventEmitter();

  apiUrl = environment.apiUrl || 'http://localhost:8080'

  constructor(
    public authService: AuthService,
    private dataService: DataService
  ) { }

  handleDelete(e: Event) {
    e.preventDefault();
    this.dataService.deletePost(this.post.filename).subscribe(() => {
      this.postDeleted.emit();
    });
  }
}
