import { Routes } from '@angular/router';

import { ProductsListComponent } from './ProductsList/ProductsList.component';
import { DetailPageComponent } from './DetailPage/DetailPage.component';
import {LoginGuard} from '../../Services/login.guard';

export const ProductsRoutes: Routes = [
	{
		path: '',
		component: ProductsListComponent
	},
	{
		path: ':id',
		component: DetailPageComponent
	},
   {
      path: 'all/list',
      component: ProductsListComponent
   }
]
