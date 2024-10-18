import { inject } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

export const AuthGuard = () => {
  const authService = inject(AuthService)
  const router = inject(Router)
  const currentUser = authService.currentUserValue

  if (currentUser) {
    return true;
  }

  router.navigate(['/login'])
  return false;
}
