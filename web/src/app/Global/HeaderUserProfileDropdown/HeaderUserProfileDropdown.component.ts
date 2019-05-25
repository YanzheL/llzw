import {Component, DoCheck, OnChanges, OnInit} from '@angular/core';
import {EmbryoService} from '../../Services/Embryo.service';
import {CanActivate, NavigationStart, Router} from '@angular/router';

@Component({
  selector: 'embryo-HeaderUserProfileDropdown',
  templateUrl: './HeaderUserProfileDropdown.component.html',
  styleUrls: ['./HeaderUserProfileDropdown.component.scss']
})
export class HeaderUserProfileDropdownComponent implements OnInit {

  rootUser: null;
   constructor(public embryoService: EmbryoService, public router: Router) { }
   ngOnInit() {
     this.rootUser = JSON.parse(localStorage.getItem('rootUser'));
     this.router.events.subscribe((event) => {
       if (event instanceof NavigationStart) {
         if ( event.url === '/home' || event.url === '/' || event.url === 'account/profile/edit?type=info') {
            this.rootUser = JSON.parse(localStorage.getItem('rootUser'));
          }
       }
     });
   }

  logout() {
     localStorage.removeItem('rootUser');
     this.rootUser = null;
     this.router.navigate(['/session/signin']);
  }


}
