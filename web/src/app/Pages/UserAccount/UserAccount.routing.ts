import { Routes } from '@angular/router';

import { AccountComponent } from './Account/Account.component';
import { ProfileComponent } from './Profile/Profile.component';
import { EditProfileComponent } from './EditProfile/EditProfile.component';
import { CardsComponent } from './Cards/Cards.component';
import { AddressComponent } from './Address/Address.component';
import { OrderHistoryComponent } from './OrderHistory/OrderHistory.component';
import {LoginGuard} from '../../Services/login.guard';

export const UserAccountRoutes : Routes = [
   {
      path : '',
      component : AccountComponent,
      children: [
         {
            path: 'profile',
            component: ProfileComponent
         },
         {
            path: 'cards',
            component: CardsComponent
         },
         {
            path: 'address',
            component: AddressComponent
         },
         {
            path: 'order-history',
            component: OrderHistoryComponent
         },
         {
            path: 'profile/edit',
            component: EditProfileComponent
         }
      ],
     canActivate: [LoginGuard]
   }
]
