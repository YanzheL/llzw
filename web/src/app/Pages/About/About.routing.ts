import { Routes } from '@angular/router';
import { AboutUsComponent } from './AboutUs/AboutUs.component';
import { ContactComponent } from './Contact/Contact.component';
import { FaqComponent } from './Faq/Faq.component';
import { TermAndConditionComponent } from './TermAndCondition/TermAndCondition.component';
import { PrivacyPolicyComponent } from './PrivacyPolicy/PrivacyPolicy.component';


export const AboutRoutes: Routes = [
	{
		path: 'about',
		component: AboutUsComponent
	},
	{
		path: 'contact',
		component: ContactComponent
	},
   {
      path: 'faq',
      component: FaqComponent
   },
   {
      path:'term-condition',
      component: TermAndConditionComponent
   },
   {
      path:'privacy-policy',
      component: PrivacyPolicyComponent
   }
]
