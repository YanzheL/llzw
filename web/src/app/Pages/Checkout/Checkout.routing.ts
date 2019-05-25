import { Routes } from '@angular/router';

import { SigninComponent } from './Signin/Signin.component';
import { PaymentComponent } from './Payment/Payment.component';
import { FinalReceiptComponent } from './FinalReceipt/FinalReceipt.component';
import {LoginGuard} from '../../Services/login.guard';

export const CheckoutRoutes: Routes = [
   {
      path : '',
      component: SigninComponent
   },
   {
      path: 'signin',
      component: SigninComponent
   },
	{
		path: 'payment',
		component: PaymentComponent,
    canActivate: [LoginGuard]
	},
   {
      path: 'final-receipt',
      component: FinalReceiptComponent,
     canActivate: [LoginGuard]
   }
]
