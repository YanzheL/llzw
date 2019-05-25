import { Routes } from '@angular/router';

import { DetailsComponent } from './Details/Details.component';

export const BlogsRoutes : Routes = [
	{
		path: 'detail',
		component: DetailsComponent
	},
   {
      path: 'detail/:id',
      component: DetailsComponent
   }
]