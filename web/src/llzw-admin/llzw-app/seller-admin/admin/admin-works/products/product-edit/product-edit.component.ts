// import { Component, OnInit } from '@angular/core';
// import { ActivatedRoute, Router } from '@angular/router';
// import { SelectionModel } from '@angular/cdk/collections';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { first } from 'rxjs/operators';
//
// import { ProductService } from '../../_services/product.service';
// import { Product } from '../../_models/product.model';
// import { usernamelValidator, namelValidator, emailValidator, matchingPasswords, sha1_64_Validator, pricelValidator } from '../../../../../appsettings/utils/app-validators';
//
//
// @Component({
//   selector: 'kt-product-edit',
//   templateUrl: './product-edit.component.html',
//   styleUrls: ['./product-edit.component.scss']
// })
// export class ProductEditComponent implements OnInit {
//
//   statusCode: number; // http 返回状态码
//   product: Product;
//   productForm: FormGroup;
//   //publicimagebaseurl: 'http://localhost:8081'
//
//   x = 5
//   y = 6
//   z = this.y + 1;
//
//   imagefiles: [
//     'http://localhost:8081/images/生鲜/巴西风味肉串/01.jpg',
//     'http://localhost:8081/images/生鲜/巴西风味肉串/02.jpg',
//     'http://localhost:8081/images/生鲜/巴西风味肉串/03.jpg',
//     'http://localhost:8081/images/生鲜/巴西风味肉串/04.jpg',
//     'http://localhost:8081/images/生鲜/巴西风味肉串/05.jpg'
//   ]; //图像文件数组(url)
//
//   constructor(
//     private formBuilder: FormBuilder,
//     private router: Router,
//     private activatedRoute: ActivatedRoute,
//     private productService: ProductService
//   ) { }
//
//
//
//   ngOnInit() {
//     let productId = window.localStorage.getItem('editProductId');
//     if (!productId) {
//       alert('Invalid action.');
//       this.router.navigate(['..'], { relativeTo: this.activatedRoute });
//       return;
//     }
//
//     /*
//     orderId	    Integer	    True    Parent Order ID
//     subject	    String	    True    ubject of this product
//     totalAmount	Float	    True    Total Amount
//     description	String	    False	Description
//     */
//
//     this.productForm = this.formBuilder.group({
//       id: [null],
//       'name': ["test_user_seller_username_1", Validators.compose([Validators.required, Validators.minLength(4), namelValidator])],
//       'valid': [true, Validators.compose([Validators.required])],
//       'price': [999999.99, Validators.compose([Validators.required, pricelValidator])],
//
//       // 'seller': [null, Validators.compose([Validators.required, Validators.minLength(4), usernamelValidator])],
//       'introduction': ["A brand new Macbook 0", Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(1024)])],
//
//       'ca': ['test_product_ca_0', Validators.compose([Validators.required, namelValidator])],
//       'caFile': ['28e51044f4a9cbae2bbd3d8a9d8c902ad1455d42208277ac4a913b003038a3dc', Validators.compose([Validators.required, sha1_64_Validator])],
//       'caId': ['test_product_caId_0', Validators.compose([Validators.required, namelValidator])],
//
//       'mainImageFiles': [],
//
//     });
//     this.productService.getById(+productId)
//       // this.productService.getById(2)
//       .subscribe(data => {
//         // 元气少女 因为此处需要编辑的控制器 少于 从 API get 到的 fields, 所以需要单独赋值
//         // this.productForm.setValue(data.result);
//         // this.productForm.setValue(data);
//         this.productForm.controls['id'].setValue(data['id']);
//         this.productForm.controls['valid'].setValue(data['valid']);
//         this.productForm.controls['name'].setValue(data['name']);
//         this.productForm.controls['seller'].setValue(data['seller']);
//         this.productForm.controls['introduction'].setValue(data['introduction']);
//         this.productForm.controls['price'].setValue(data['price']);
//         this.productForm.controls['ca'].setValue(data['ca']);
//         this.productForm.controls['caFile'].setValue(data['caFile']);
//         this.productForm.controls['caId'].setValue(data['caId']);
//       });
//   }
//
//   /*
//
//             .subscribe(
//             successCode => {
//        this.statusCode = successCode;
//        this.getAllArticles();
//        this.backToCreateArticle();
//   },
//   errorCode => this.statusCode = errorCode);
//   */
//
//   onSubmit() {
//
//     //验证不通过 不上传
//     if (this.productForm.invalid) {
//       return;
//     }
//
//     this.productService.update(this.productForm.value)
//       .pipe(first())
//       .subscribe(
//         success => {
//           this.statusCode = success.status;
//           alert('Product updated successfully.');
//           console.log('successCode:', this.statusCode);
//           this.router.navigate(['..'], { relativeTo: this.activatedRoute });
//
//         },
//         error => {
//           this.statusCode = error.status;
//           alert('errorCode' + this.statusCode);
//         });
//   }
//
// }
