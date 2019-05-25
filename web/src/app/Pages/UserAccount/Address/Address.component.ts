import { Component, OnInit } from '@angular/core';
import {EmbryoService} from '../../../Services/Embryo.service';

@Component({
  selector: 'app-Address',
  templateUrl: './Address.component.html',
  styleUrls: ['./Address.component.scss']
})
export class AddressComponent implements OnInit {
  data = {'owner': ''}
  displayedColumns: string[] = ['id', 'province', 'city', 'district', 'address', 'zip'];
  dataSource = [
    {'id': '0', province: 1, city: 'City', district: 1.0079, address: 'H', zip: '435200'},

  ];
  constructor(public embryoService: EmbryoService) { }

  ngOnInit() {
    this.getUserAddress();
  }
  getUserAddress() {
    this.data.owner = JSON.parse(localStorage.getItem('rootUser')).username;
    this.embryoService.getUserAddress(this.data).subscribe(res =>{ this.dataSource = res.data; });
  }
}
