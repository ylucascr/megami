import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent {
  @Input({ required: true }) posts: any[] = [];
  @Input({ required: true }) isFetching = true;
  @Input() emptyListTxt: string | null = null;

  @Output() postDeleted: EventEmitter<any> = new EventEmitter();

  constructor(
    public authService: AuthService
  ) { }

  handleDelete() {
    this.postDeleted.emit();
  }
}