import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { DataService } from '../data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-upload-form-page',
  templateUrl: './upload-form-page.component.html'
})
export class UploadFormPageComponent implements OnInit {
  post: any = null;
  isFetching = false;
  isEditing = false;

  form: FormGroup = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    file: new FormControl(''),
    fileSrc: new FormControl<File | null>(null),
  });

  constructor(
    public router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private dataService: DataService
  ) { }

  ngOnInit(): void {
    this.isEditing = !this.router.url.includes('form');
    
    if (this.isEditing) {
      this.route.paramMap.pipe(
        switchMap(params => this.dataService.getPost(params.get('filename')!))
      ).subscribe({
        error: () => this.router.navigate(['/']),
        next: res => {
          this.post = res.data;

          if (this.post.uploader !== this.authService.loggedInUser.getValue()) {
            this.router.navigate(['/']);
          }

          const { title, description } = this.post;

          this.form.patchValue({
            title,
            description
          });
        }
      });
    }
  }

  onFileChange(e: Event) {
    const element = e.target as HTMLInputElement;
    const files = element.files;

    if (files) {
      this.form.patchValue({
        fileSrc: files[0]
      });
    }
  }

  handleSubmit() {
    if (this.form.invalid) return;
    
    const { title, description, fileSrc: file } = this.form.value;

    const formData = new FormData();
    formData.set('title', title);
    if (file !== null)
      formData.set('file', file);
    formData.set('description', description);

    this.isFetching = true;
    (this.isEditing ?
      this.dataService.updatePost(this.post.filename, formData) :
      this.dataService.uploadPost(formData)
    ).subscribe(res => {
      if (res.status === 'success') {
        this.router.navigate(['/']);
      }
    });
  }
}
