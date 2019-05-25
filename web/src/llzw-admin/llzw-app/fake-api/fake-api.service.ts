// Angular
import { Injectable } from '@angular/core';
// Angular in memory
import { InMemoryDbService } from 'angular-in-memory-web-api';
// RxJS
import { Observable } from 'rxjs';
// Auth
// import { AuthDataContext } from '../../../../auth'; // 元气少女

// Models
import { CarsDb } from './fake-db/cars';
import { ProductsDb } from './fake-db/products';
import { StocksDb } from './fake-db/stocks';
import { PaymentsDb } from './fake-db/payments';


@Injectable()
export class FakeApiService implements InMemoryDbService {
	/**
	 * Service Constructore
	 */
  constructor() { }

	/**
	 * Create Fake DB and API
	 */
  createDb(): {} | Observable<{}> {
    // tslint:disable-next-line:class-name
    const db = {
      // auth module
      // users: AuthDataContext.users,  // 元气少女
      // roles: AuthDataContext.roles, // 元气少女
      // permissions: AuthDataContext.permissions, // 元气少女


      // data-table
      cars: CarsDb.cars,

      // data-table
      products: ProductsDb.products, // 假数据
      stocks: StocksDb.stocks, // 假数据
      payments: PaymentsDb.payments // 假数据
    };
    return db;
  }
}
