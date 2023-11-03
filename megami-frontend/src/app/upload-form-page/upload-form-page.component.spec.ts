import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadFormPageComponent } from './upload-form-page.component';

describe('UploadFormPageComponent', () => {
  let component: UploadFormPageComponent;
  let fixture: ComponentFixture<UploadFormPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadFormPageComponent]
    });
    fixture = TestBed.createComponent(UploadFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
