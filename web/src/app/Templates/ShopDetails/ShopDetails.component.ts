import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {EmbryoService} from '../../Services/Embryo.service';

import {environment} from '../../../environments/environment';

declare var $: any;


const BASE_FILE_URL = environment.url1 + '/files';  // 中文
// 上传图片用的卡片。
class UpLoadImg {
    // 有远程优先用远程
    isEmptyNewImg = false;
    isFromRemote = true;
    localUrl; // 上传前用于本地预览
    url: string // 来自远程
    relativePath: string;
    hash: string;
    obj: Object;
    // 时间戳  同一张卡片有本地数据有远程数据，本地 数据 先于 远程数据存入卡片，中间有时间差
    // 时间戳 用于上传图片后再找到 用本地图片 的 卡片编号
    timestamp: number = 0;
}

// 商品介绍
interface Details {
    brand: string;              // '品牌': '恒都';
    name: string;               // '商品名称': '恒都澳洲牛腱子1kg 整肉';
    number: string;             // '商品编号': '3043161';
    gross: string;              // '商品毛重': '1.03kg';
    saveStatus: string;         // '保存状态': '冷冻';
    netWeight: string;          // '重量': '501g-1kg';
    domesticImport: string;     //  '国产/进口': '进口';
    variety: string;            // '品种': '其他';
    feedingMethod: string;      // '饲养方式': '草饲';
    classification: string;     // '分类': '牛腱';
    package: string;            // '包装': '简装';
    torageConditions: string;   // '贮存条件': '深冷、冷冻 -18℃';
    shelfLife: string;          // '保质期': '365天';
    netContent: string;         // '净含量': '1000g';
    packingList: string;        // '包装清单': '澳洲牛腩块 冷冻肉 1kg×1';
    imgs: string[];              // 商品详情中的大图
}


@Component({
    selector: 'embryo-ShopDetails',
    templateUrl: './ShopDetails.component.html',
    styleUrls: ['./ShopDetails.component.scss']
})
export class ShopDetailsComponent implements OnInit, OnChanges {

    @Input() detailData: any;
    @Input() currency: string;
    base_file_url = BASE_FILE_URL;
    details: Details; // 接口 商品详情，用于解析 introduct 字段
    bigImgs: UpLoadImg[] = []; // TODO: 图像文件数组(url) 必须这样初始化，否则后面用不了
    seller_nickname: string; // 卖家 昵称
    mainImgPath: string;
    totalPrice: any;
    type: any;
    colorsArray: string[] = ["Red", "Blue", "Yellow", "Green"];
    sizeArray: number[] = [36, 38, 40, 42, 44, 46, 48];
    quantityArray: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    productReviews: any;
    images = [
        'assets/images/men/3-item-a.jpg',
        'assets/images/men/3-item-b.jpg',
        'assets/images/men/3-item-c.jpg',
        'assets/images/men/3-item-d.jpg',
        'assets/images/men/3-item-e.jpg'
    ];
    chipclass = ['chipclass1', 'chipclass2', 'chipclass3', 'chipclass4']
    @Input() labels;
    @Input() category;

    constructor(private route: ActivatedRoute,
                private router: Router,
                public embryoService: EmbryoService
    ) {
        this.embryoService.getProductReviews().valueChanges().subscribe(res => {
            this.productReviews = res;

        });
    }

    ngOnInit() {
        this.mainImgPath = this.detailData.mainImageFiles ? this.base_file_url + '/' + this.detailData.mainImageFiles[0] : '';
        this.totalPrice = this.detailData.price;


        this.route.params.subscribe(res => {
            this.type = null;
            this.type = res.type;
        });
        // this.setQuantityArray();
    }

    // setQuantityArray () {
    //    let num = 10;
    //    if ( this.stocks! = null && this.stocks.currentQuantity < num) {
    //      num = this.stocks.currentQuantity;
    //    }
    //    for (let i = 1; i <= num; i++) {
    //      this.quantityArray[i] = i;
    //    }
    // }
    ngOnChanges() {
        this.mainImgPath = null;
        this.totalPrice = null;

        this.totalPrice = this.detailData.price;

        this.mainImgPath = this.detailData.mainImageFiles ? this.base_file_url + '/' + this.detailData.mainImageFiles[0] : '';

        console.log("取商品详情数据 反序列化前  this.detailData=", this.detailData);

        // 取商品详情数据 反序列化 😂😂😂😂😂😂😂
        try {
            // alert(data['introduction']);
            let obj_details: Details = JSON.parse(this.detailData['introduction']);


            console.log("取商品详情数据 反序列化后 = ", obj_details);
            this.details = obj_details;


            // 将商品详情 填入表单
            // this.isFormInitFromGet = true; // 监听功能失效 this.detailsForm.valueChanges.subscribe
            //this.details_to_form();
            // this.isFormInitFromGet = false; // 监听功能生效 this.detailsForm.valueChanges.subscribe
            // 商品详情表单填写结束


            // 取商品详情图像文件， 图像不用表单项控制器管理
            if (this.details.imgs) {

                this.details.imgs.forEach(
                    (hash) => {
                        let temp = new UpLoadImg();
                        temp.hash = hash;
                        temp.url = this.base_file_url + '/' + hash; //图片url
                        console.log('  temp.url: ', temp.url);

                        this.bigImgs.push(temp);  // 尾部插入
                    }
                )
            }
        } catch (e) {
            // document.writeln("Caught: " + e.message); // 这行语句会生成个错误提示页面，不是咱想用的
            console.log(' Caught: ', e.message);
        }
        // console.log('this.details = ', this.details);
    }

    /**
     * getImagePath is used to change the image path on click event.
     */
    public getImagePath(imgPath: string, index: number) {
        $('.p-link').removeClass('border-active');
        this.mainImgPath = imgPath;
        $("#" + index + "_img").addClass('border-active');
    }

    public calculatePrice(detailData: any, value: any) {
        detailData['quantity'] = value;
        this.totalPrice = detailData.price * value;
    }

    public reviewPopup(detailData) {
        let reviews: any = null;
        for (let review of this.productReviews) {
            // if((review.id == detailData.id) && (review.type == detailData.type) && (review.category == detailData.category)){
            //    singleProduct = review;
            //    break;
            // }

            reviews = review.user_rating;
        }

        this.embryoService.reviewPopup(detailData, reviews);
    }

    public addToWishlist(value: any) {
        this.embryoService.addToWishlist(value);
    }

    public addToCart(value: any) {
        this.embryoService.addToCart(value);
    }

    public buyNow(value: any) {
        this.embryoService.buyNow(value);
        this.router.navigate(['/checkout']);
    }

}
