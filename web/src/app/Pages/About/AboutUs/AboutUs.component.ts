import { Component, OnInit } from '@angular/core';
import { EmbryoService } from '../../../Services/Embryo.service';


@Component({
  selector: 'app-AboutUs',
  templateUrl: './AboutUs.component.html',
  styleUrls: ['./AboutUs.component.scss']
})
export class AboutUsComponent implements OnInit {

   teamData          : any;
   testimonialData   : any;
   missionVisionData : any;
   aboutInfo         : any;


   constructor(private embryoService : EmbryoService) { }

   ngOnInit() {
      this.getAboutInfo();
      this.getMissionVision();
      this.getTestimonialData();
      this.getTeamData();
   }

   public getAboutInfo() {
      this.embryoService.getAboutInfo().valueChanges().subscribe(res => {this.aboutInfo = res});
   }

   public getMissionVision() {
      this.embryoService.getMissionVision().valueChanges().subscribe(res => {this.missionVisionData = res});
   }

   public getTeamData() {
      this.embryoService.getTeam().valueChanges().subscribe(res => {this.teamData = res});
   }

   public getTestimonialData() {
      this.embryoService.getTestimonial().valueChanges().subscribe(res => {this.testimonialData = res});
   }
}

