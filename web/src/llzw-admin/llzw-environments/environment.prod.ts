
import { seller_apiUrl } from '../../environments/environment.prod'

export const environment = {
	production: true,
	isMockEnabled: true, // You have to switch this, when your real back-end is done
	authTokenKey: 'authce9d77b308c149d5992a80073637e4d5'
};

export const apiUrl = seller_apiUrl;
