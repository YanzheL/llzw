import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params }   from '@angular/router';
import { FormControl, FormGroup, FormBuilder,FormArray, Validators } from '@angular/forms';
import { ToastaService, ToastaConfig, ToastOptions, ToastData} from 'ngx-toasta';

@Component({
  selector: 'app-EditProfile',
  templateUrl: './EditProfile.component.html',
  styleUrls: ['./EditProfile.component.scss']
})
export class EditProfileComponent implements OnInit {

   type         : string;
   info         : FormGroup;
   address      : FormGroup;
   card         : FormGroup;
   emailPattern : any = /\S+@\S+\.\S+/;
   toastOption  : ToastOptions = {
      title     : "Account Information",
      msg       : "Your account information updated successfully!",
      showClose : true,
      timeout   : 3000,
      theme     : "material"
   };

   constructor(private route: ActivatedRoute,
               private router: Router,
               private formGroup : FormBuilder,
               private toastyService: ToastaService) {

      this.route.params.subscribe(params => {
         this.route.queryParams.forEach(queryParams => {
            this.type = queryParams['type'];
         });   
      });
   }

   ngOnInit() {
      this.info = this.formGroup.group({
         first_name   : ['', [Validators.required]],
         last_name    : ['', [Validators.required]],
         gender       : ['male'],
         date         : [],
         phone_number : ['', [Validators.required]],
         location     : [''],
         email        : ['', [Validators.required, Validators.pattern(this.emailPattern)]]
      });

      this.address = this.formGroup.group({
         address      : ['', [Validators.required]],
         buiding_name : ['', [Validators.required]],
         street_no    : ['', [Validators.required]],
         state        : ['', [Validators.required]],
         zip_code     : ['', [Validators.required]],
         country      : ['', [Validators.required]]
      });

      this.card = this.formGroup.group({
         card_number : ['', [Validators.required]],
         cvv         : ['', [Validators.required]],
         name        : ['', [Validators.required]],
         month       : ['', [Validators.required]],
         year        : ['', [Validators.required]]
      })
   }

   /**
    * Function is used to submit the profile info.
    * If form value is valid, redirect to profile page.
    */
   submitProfileInfo() {
      if(this.info.valid){
         this.router.navigate(['/account/profile']).then(()=>{
           this.toastyService.success(this.toastOption);
         });
      } else {
         for (let i in this.info.controls) {
            this.info.controls[i].markAsTouched();
         }
      }
   }

   /**
    * Function is used to submit the profile address.
    * If form value is valid, redirect to address page.
    */
   submitAddress() {
      if(this.address.valid){
         this.router.navigate(['/account/address']).then(()=>{
           this.toastyService.success(this.toastOption);
         });
      } else {
         for (let i in this.address.controls) {
            this.address.controls[i].markAsTouched();
         }
      }
   }

   /**
    * Function is used to submit the profile card.
    * If form value is valid, redirect to card page.
    */
   submitCard() {
      if(this.card.valid) {
         this.router.navigate(['/account/card']).then(()=>{
           this.toastyService.success(this.toastOption);
         });
      } else {
         for(let i in this.card.controls) {
            this.card.controls[i].markAsTouched();
         }
      }
   }

}
