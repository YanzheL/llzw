import { Component, OnInit } from '@angular/core';
import {EmbryoService} from '../../../Services/Embryo.service';

@Component({
  selector: 'app-Profile',
  templateUrl: './Profile.component.html',
  styleUrls: ['./Profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user_info = {};
   constructor(public embryoService: EmbryoService) { }

   getUserInfo() {
     this.embryoService.getUserInfo().subscribe(res => {this.user_info = res.data; });
   }
   ngOnInit() {
     this.getUserInfo();
   }

}
