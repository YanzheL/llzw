import { Routes } from '@angular/router';

import { RegisterComponent } from './Register/Register.component';
import { SignInComponent } from './SignIn/SignIn.component';
import { ForgotPasswordComponent } from './ForgotPassword/ForgotPassword.component';
import { ThankYouComponent } from './ThankYou/ThankYou.component';

export const SessionRoutes : Routes = [
	{ 
		path: '', 
		component: SignInComponent 
	},
   { 
      path: 'signin', 
      component: SignInComponent 
   },
	{
		path: 'signup',
		component: RegisterComponent
	},
   {
      path: 'thank-you',
      component: ThankYouComponent
   },
   {
      path: 'forgot-password',
      component: ForgotPasswordComponent
   }
]