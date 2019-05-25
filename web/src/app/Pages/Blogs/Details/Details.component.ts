import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params }   from '@angular/router';
import { FormControl, FormGroup, FormBuilder,FormArray, Validators } from '@angular/forms';
import { EmbryoService } from '../../../Services/Embryo.service';

@Component({
  selector: 'app-Details',
  templateUrl: './Details.component.html',
  styleUrls: ['./Details.component.scss']
})
export class DetailsComponent implements OnInit {

   commentForm  : FormGroup;
   response    : any;
   blogDetails : any;
   blogId      : any;
   emailPattern : any = /\S+@\S+\.\S+/;

   constructor(public embryoService : EmbryoService,
              private route: ActivatedRoute,
              private router: Router,
              private formGroup : FormBuilder) {
   }

   ngOnInit() {
      this.commentForm = this.formGroup.group({
         first_name : [''],
         last_name  : [''],
         email      : ['', Validators.pattern(this.emailPattern)],
         message    : ['']
      })

      this.route.params.subscribe(res => {
         this.blogId = res.id;
         this.getBlodDetails();
      });
   }

   public getBlodDetails() {
      this.blogId = (this.blogId) ? this.blogId : 1;
      this.embryoService.getBlogList().valueChanges().
        subscribe(res => {this.getBlogDetailsResponse(res)});
   }

   public getBlogDetailsResponse(response) {
      for(let data of response)
      {
         if(data.id == this.blogId) {
            this.blogDetails = data;
            break;
         }
      }
   }

   public submitForm() {
      console.log(this.commentForm.value);
   }
  
}
