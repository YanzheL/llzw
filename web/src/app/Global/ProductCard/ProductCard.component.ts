import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {EmbryoService} from '../../Services/Embryo.service';

declare var $: any;

@Component({
    selector: 'embryo-ProductCard',
    templateUrl: './ProductCard.component.html',
    styleUrls: ['./ProductCard.component.scss']
})
export class ProductCardComponent implements OnInit {

    @Input() product: any;

    @Input() index: any;

    @Input() currency: string;

    @Output() addToCart: EventEmitter<any> = new EventEmitter();

    @Output() addToWishlist: EventEmitter<any> = new EventEmitter();

    constructor(public embryoService: EmbryoService) {
    }

    ngOnInit() {
    }

    public addToCartProduct(value: any) {
        this.addToCart.emit(value);
    }

    public productAddToWishlist(value: any, parentClass) {
        if (!($('.' + parentClass).hasClass('wishlist-active'))) {
            $('.' + parentClass).addClass('wishlist-active');
        }

        this.addToWishlist.emit(value);
    }

    public checkCartAlready(singleProduct) {
        let products = JSON.parse(localStorage.getItem("cart_item")) || [];
        if (!products.some((item) => item.id == singleProduct.id)) {
            return true;
        }
    }

}
