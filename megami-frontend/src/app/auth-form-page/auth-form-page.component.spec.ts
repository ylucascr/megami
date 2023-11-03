import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthFormPageComponent } from './auth-form-page.component';

describe('AuthFormPageComponent', () => {
  let component: AuthFormPageComponent;
  let fixture: ComponentFixture<AuthFormPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AuthFormPageComponent]
    });
    fixture = TestBed.createComponent(AuthFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
