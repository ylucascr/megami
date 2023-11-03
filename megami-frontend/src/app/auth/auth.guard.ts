import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return authService.isLoggedIn().pipe(
    map(isLoggedIn => {
      if (isLoggedIn) return true;
      return router.parseUrl('/auth');
    })
  );
};

export const loggedInRedirect: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const authService: AuthService = inject(AuthService);

  return authService.isLoggedIn().pipe(
    map(isLoggedIn => {
      if (isLoggedIn) return router.parseUrl('/');
      return true;
    })
  )
};
