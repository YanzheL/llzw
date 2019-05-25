import {CanActivate} from '@angular/router';

export class LoginGuard implements CanActivate {
  canActivate() {
    if (localStorage.getItem('rootUser')) {
      return true;
    } else {
      return false;
    }
  }

}
